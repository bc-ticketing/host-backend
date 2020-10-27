# host-backend
Spring Boot application for managing access control

### Description

This Spring boot application is the backend of our access control implementation. In order for a guest to access an event, he has to prove ownership over his ETH address.
To do this, he will be prompted with a random string. He then needs to sign this string using the eth address holding the tickets.
This will proof ownership of the address.

### Implementation

#### Terminal Part

The String will be presented as a QR code. This will be done by our terminals. These terminals interact via the following way:

Following is the representation of Terminal in the backend:

| Type         | Name          | Description                                                         | Annotation                     |
|--------------|---------------|---------------------------------------------------------------------|--------------------------------|
| UUID         | terminalID    | unique identifier of the terminal                                   | auto generated                 |
| String       | randID        | Random Value presented to guest to sign                             | current length 42              |
| List<String> | ticketType    | List of ticket types required to access area controlled by terminal | empty list if general entrance |
| String       | areaAccessTo  | Area the guest is in, after he passed this terminal                 |                                |
| boolean      | responseDone  | flag to save whether a response has been done to this randID        | default false                  |
| boolean      | accessAllowed | flag to indicate, whether the guest is allowed to enter             | default false                  |

A Terminal uses the following Post to register :

| Parameter               | Value                                                                   |
|-------------------------|-------------------------------------------------------------------------|
| URL                     | "/registerTerminal"                                                     |
| String secret           | secret Token to add terminal                                            |
| List<String> ticketType | ticket type required to access the area (empty list if general access)  |
|                         |                                                                         |
| Return                  | TerminalEntity                                                          |

Response is the terminal Entity

Whenever a terminal has been registered, an initial random string has been generated. The terminal can then display this string.

At the same time, it should start querying following request:

| Parameter       | Value                       |
|-----------------|-----------------------------|
| URL             | "/getTerminalStatus"        |
| UUID terminalId | terminal ID of the terminal |
|                 |                             |
| Return          | TerminalEntity              |

This will return the terminal Entity. 
As already described, the guest will submit a post request with the signed data.
This will set the boolean responseDone of the terminal entity to true.

The backend will evaluate, whether the guest has a valid ticket.
When the guest has authorization to access, the boolean accessAllowed will be set to true.

Once the responseDone flag changes, the Terminal will have to show the access status.

After that, the terminal can request a new random string via this post:

| Parameter       | Value                       |
|-----------------|-----------------------------|
| URL             | "/NewSecretCode"         |
| UUID terminalId | terminal ID of the terminal |
|                 |                             |
| Return          | TerminalEntity              |

This will save the guest as accessed to the area the terminal grants access to and return a new random string.

Here the process starts from the beginning.

To unregister a terminal, use:

| Parameter       | Value                       |
|-----------------|-----------------------------|
| URL             | "/unRegisterTerminal"       |
| UUID terminalId | terminal ID of the terminal |
|                 |                             |
| Return          | boolean                     |


#### Guest Part



