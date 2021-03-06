#include <utility>

#include "radio.hpp"


// starts the mesh network
void Radio::beginMesh(uint8_t nodeID) {
    _mesh.setNodeID(nodeID);
    if (nodeID != 0) {
        printf("Connecting to the mesh...\n");
    } else {
        printf("Creating mesh ...\n");
    }
    _mesh.begin();
    _rf24.setPALevel(RF24_PA_HIGH);
    _rf24.setDataRate(RF24_1MBPS);
    _rf24.setCRCLength(RF24_CRC_16);
}

// accepts the callback and runs it
void Radio::registrate(void (*registrationFunction)()) {
    this->_registrationFunction = registrationFunction;
    _registrationFunction();
}

void Radio::setRequestCallback(void (*requestCallback)(request_payload_struct, RF24NetworkHeader)) {
    this->_requestCallback = requestCallback;
}

void Radio::setResponseCallback(void (*responseCallback)(response_payload_struct, RF24NetworkHeader)) {
    this->_responseCallback = responseCallback;
}

void Radio::setRegistrationCallback(void (*registrationCallback)(registration_payload_struct, RF24NetworkHeader)) {
    this->_registrationCallback = registrationCallback;
}

void Radio::setCommandCallback(void (*commandCallback)(command_payload_struct, RF24NetworkHeader)) {
    this->_commandCallback = commandCallback;
}

// return if this node is a master node by checking the node_id
bool Radio::isMaster() {
    return this->_mesh.getNodeID() == 0;
}

// function which updates the network, if available reads from the network and calls the callbacks
// also run the DHCP server if this is the master node
void Radio::update() {
    this->_mesh.update();

    if (this->isMaster()) {
        _mesh.DHCP();
    }

    this->checkConnection();

    while (this->packageAvailable()) {
        RF24NetworkHeader header = this->peekHeader();
        switch (header.type) {
            case request_symbol:
                _requestCallback(this->readRequest(), header);
                break;
            case response_symbol:
                _last_response = response_payload_struct();
                _last_response = this->readResponse();
                _responseCallback(_last_response, header);
                break;
            case registration_symbol:
                _registrationCallback(this->readRegistration(), header);
                break;
            case command_symbol:
                _commandCallback(this->readCommand(), header);
                break;
            default:
                _network.read(header, nullptr, 0);
                break;
        }
    }
}

// return if a package is available for this node
bool Radio::packageAvailable() {
    return _network.available();
}

// function for only peeking the header and not reading it.
// useful for type requests before reading the payload into the datatype
RF24NetworkHeader Radio::peekHeader() {
    RF24NetworkHeader header;
    _network.peek(header);
    return header;
}

/**** BASIC READING FUNCTIONS *****/

// function for receiving requests
request_payload_struct Radio::readRequest() {
    RF24NetworkHeader header;
    request_payload_struct payload{};
    _network.read(header, &payload, sizeof(payload));
    return payload;
}

// function for receiving responses
response_payload_struct Radio::readResponse() {
    RF24NetworkHeader header;
    response_payload_struct payload{};
    _network.read(header, &payload, sizeof(payload));
    return payload;
}

// function for receiving commands
command_payload_struct Radio::readCommand() {
    RF24NetworkHeader header;
    command_payload_struct payload{};
    _network.read(header, &payload, sizeof(payload));
    return payload;
}

// function for receiving registrations
registration_payload_struct Radio::readRegistration() {
    RF24NetworkHeader header;
    registration_payload_struct payload{};
    _network.read(header, &payload, sizeof(payload));
    return payload;
}

/**** END ****/

/**** BASIC SENDING FUNCTIONS *****/

unsigned long Radio::sendRadioPayload(RadioPayload &payload) noexcept(false) {
    bool taskIsRunning = _taskIsRunning;
    if (taskIsRunning && payload.getType() != PayloadType::RESPONSE)
        this->stopTask();

    if (this->isValidNode(payload.getToNode())) {
        if (_mesh.write(payload.asSendable(), payload.getSymbol(), payload.sizeOfPayload(), payload.getToNode())) {
            payload.printPayload(true);
        } else {
            this->checkConnection();
            _last_failed_request_id = payload.request_id;

            if (taskIsRunning && payload.getType() != PayloadType::RESPONSE) this->updateAndLog();
            throw PayloadNotSendableException(payload.request_id);
        }
    } else {
        if (taskIsRunning && payload.getType() != PayloadType::RESPONSE) this->updateAndLog();
        throw InvalidNodeException(payload.request_id);
    }

    if (taskIsRunning && payload.getType() != PayloadType::RESPONSE) this->updateAndLog();
    return payload.request_id;
}

/**** END ****/

/*** additional functions for requests ****/
unsigned long Radio::sendRequest(string attribute_requested, string additional_value,
                                 uint16_t node) noexcept(false) {
    RequestPayload payload;
    payload.request_id = this->generateRequestID();
    payload.attribute_requested = std::move(attribute_requested);
    payload.additional_value = std::move(additional_value);
    payload.setToNode(node);
    sendRadioPayload(payload);
    return payload.request_id;
}

unsigned long Radio::sendRequest(string attribute_requested, uint16_t node) noexcept(false) {
    return sendRequest(std::move(attribute_requested), "", node);
}
/**** END ****/

/**** additional functions for responses ****/
unsigned long
Radio::sendResponse(string value, radio_payload_struct &r_payload, int16_t node) noexcept(false) {
    ResponsePayload payload;
    payload.request_id = r_payload.request_id;
    payload.value = std::move(value);
    payload.setToNode(node);

    return sendRadioPayload(payload);
}

// function for sending responses with a RF24NetworkHeader and the value given
unsigned long Radio::sendResponse(string value, radio_payload_struct &payload,
                                  RF24NetworkHeader &header) noexcept(false) {
    return sendResponse(std::move(value), payload, _mesh.getNodeID(header.from_node));
}

// function for sending standardized responses
unsigned long Radio::sendSimpleResponse(SimpleResponse type, radio_payload_struct &payload,
                                        RF24NetworkHeader &header) noexcept(false) {
    switch (type) {
        case SimpleResponse::ERROR:
            return this->sendResponse("ERROR", payload, header);
        case SimpleResponse::OK:
            return this->sendResponse("OK", payload, header);
        default:
            return 0;
    }
}

/**** END ****/

/**** additional functions for commands ****/

// function for sending commands without the struct given instead command and additional_value
unsigned long
Radio::sendCommand(string command, string additional_value, uint16_t node) noexcept(false) {
    CommandPayload payload;
    payload.request_id = this->generateRequestID();
    payload.command = std::move(command);
    payload.additional_value = std::move(additional_value);
    payload.setToNode(node);

    return sendRadioPayload(payload);
}

// function for sending commands without the struct and additional_value given instead command only
unsigned long Radio::sendCommand(string command, uint16_t node) noexcept(false) {
    return sendCommand(std::move(command), "", node);
}

/**** END ****/

/**** additional functions for registrations ****/

// function for sending registrations
unsigned long Radio::sendRegistration(ModuleType type, int index, int pin) noexcept(false) {
    RegistrationPayload payload;
    payload.request_id = this->generateRequestID();
    payload.module_type = type;
    payload.index = index;
    payload.pin = pin;
    return sendRadioPayload(payload);
}

// function for sending registrations
unsigned long Radio::sendRegistration(ModuleType type) noexcept(false) {
    return sendRegistration(type, -1, -1);
}

/**** END ****/

// function for generating request_ids based on request_counter and node_id
unsigned long Radio::generateRequestID() {
    unsigned long request_id = 0;
    request_id += _mesh.getNodeID();
    request_id += (_request_counter * 100);
    _request_counter++;
    return request_id;
}

// function for checking connection and reconnecting as well as reregistration at master if necessary
void Radio::checkConnection() {
    if (!this->isMaster()) {
        if (!_mesh.checkConnection()) {
            printf("Reconnecting ...\n");
            _mesh.renewAddress();
            this->_registrationFunction();
        }
    }
}

// function which takes a request_id and waits for a response with this id
response_payload_struct Radio::waitForAnswer(unsigned long searched_request_id) noexcept(false) {

    if (!_taskIsRunning) {
        this->updateAndLog();
    }

    unsigned long timout = 5000;
    response_payload_struct nullPayload{};
    strcpy(nullPayload.value, "");

    if (_last_failed_request_id == searched_request_id) {
        throw ResponseNotFoundException(searched_request_id);
    }
    unsigned long startTime = millis();
    while ((millis() - startTime) < timout) {
        if (_last_response.request_id == searched_request_id) {
            if (string(_last_response.value) == "ER") {
                if (_last_response.additional_information == AdditionalInformation::INVALIDINDEX) {
                    throw InvalidIndexException(searched_request_id);
                }
            }


            return _last_response;
        }
    }
    throw ResponseNotFoundException(searched_request_id);
}

bool Radio::isValidNode(uint16_t node) {
    for (int i = 0; i < _mesh.addrListTop; i++) {
        if (_mesh.addrList[i].nodeID == node) {
            return true;
        }
    }
    return false;
}

int16_t Radio::getNodeID(uint32_t adress) {
    return _mesh.getNodeID(adress);
}

void Radio::updateAndLog() {
    Radio *this_radio = this;
    _cts = cancellation_token_source();
    auto token = _cts.get_token();

    _background = create_task([this_radio, token] {
        while (true) {
            if (token.is_canceled()) {
                printf("*** Stopping task\n");
                cancel_current_task();
            } else {
                this_radio->update();
            }
        }
    }, token);
    _taskIsRunning = true;

    printf("*** Starting Task\n");
}

void Radio::stopTask() {
    _cts.cancel();
    _background.wait();
    _taskIsRunning = false;
}

vector<int> Radio::listAllDevices() {
    vector<int> devices;

    for (int i = 0; i < _mesh.addrListTop; i++) {
        devices.push_back(_mesh.addrList[i].nodeID);
    }

    return devices;
}
