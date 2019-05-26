#include <utility>

#ifndef EXCEPTIONS_H
#define EXCEPTIONS_H

#include <string>
#include <utility>
#include <cpprest/json.h>

using namespace std;
using namespace web;

class PrintableException : public std::exception {
protected:
    std::string _type = "";
    std::string _message = "";
public:
    explicit PrintableException(string type) : _type(std::move(type)) {}

    PrintableException(string type, string message) : _type(std::move(type)), _message(std::move(message)) {}

    std::string getMessage() {
        return _message;
    }

    std::string getType() {
        return _type;
    };

    virtual std::string getException() {
        stringstream strStream;
        strStream << this->getType() << " " << this->getMessage();
        return strStream.str();
    }

    void printException() {
        std::cout << "!!!" << this->getException() << "!!!" << std::endl;
    }

    virtual json::value asJson() {
        json::value error;
        error["error"] = json::value();
        error["error"]["type"] = json::value::string(this->getType());
        error["error"]["internalMessage"] = json::value::string(this->getMessage());
        return error;
    }
};

class PrintableMeshException : public PrintableException {
protected:
    unsigned long _request_id = 0;
public:
    PrintableMeshException(const string &type, unsigned long requestId) : PrintableException(type),
                                                                          _request_id(requestId) {}

    PrintableMeshException(const string &type, const string &message, unsigned long requestId) : PrintableException(
            type, message), _request_id(requestId) {}


    unsigned long getRequestID() {
        return _request_id;
    }

    string getException() override {
        stringstream strStream;
        strStream << PrintableException::getException() << " (#" << _request_id << ")";
        return strStream.str();
    }

    json::value asJson() override {
        json::value error = PrintableException::asJson();
        error["error"]["request_id"] = json::value::number((uint32_t) this->getRequestID());
        return error;
    }
};

class ResponseNotFoundException : public PrintableMeshException {
public:
    explicit ResponseNotFoundException(unsigned long requestId) : PrintableMeshException(string("ResponseNotFound"),
                                                                                         string("Could not get the response"),
                                                                                         requestId) {

    }
};

class PayloadNotSendableException : public PrintableMeshException {
protected:
    PayloadNotSendableException(const string &type, const string &message, unsigned long requestId)
            : PrintableMeshException(type, message, requestId) {}

public:
    explicit PayloadNotSendableException(unsigned long requestId) : PrintableMeshException(string("PayloadNotSendable"),
                                                                                           string("This payload could not be sent"),
                                                                                           requestId) {}

    PayloadNotSendableException(const string &message, unsigned long requestId) : PrintableMeshException(
            string("PayloadNotSendable"), message, requestId) {}


};

class InvalidIndexException : public PrintableMeshException {
public:
    explicit InvalidIndexException(unsigned long requestId) : PrintableMeshException(string("InvalidIndex"),
                                                                                     string("The given index is invalid"),
                                                                                     requestId) {
    }
};

class InvalidNodeException : public PayloadNotSendableException {
public:
    explicit InvalidNodeException(unsigned long requestId) : PayloadNotSendableException(string("InvalidNode"),
                                                                                         string("The given node is invalid"),
                                                                                         requestId) {
    }

    InvalidNodeException(const string &message, unsigned long requestId) : PayloadNotSendableException(
            string("InvalidNode"),
            message,
            requestId) {}
};

#endif