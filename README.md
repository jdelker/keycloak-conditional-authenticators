# keycloak-conditional-authenticators

> Additional *Conditional Keycloak Authenticator* modules to be used in the authentication flow

When it comes to conditional authentication in a keycloak authentication flow, there are very little options available.
Beginning with Keycloak 10, support for conditional flow executions have been added, that allow a much more flexible way to define conditions for existing authentication modules.
Multiple *Condition Modules* can now be combined together in a sub-flow to define a logical AND condition.
Unfortunately, there are not very many of those modules available (at least up to Keycloak 11) to get a useful scenario out of that.

This project shall serve as a container for additional *Conditional Modules*. 
Currently contained:

- ConditionalHeaderAuthenticator: Matches the request HTTP headers against a given expression


## Build 

To build the JAR module, invoke
```sh
mvn package
```

This will download all required dependencies and build the JAR in the `target` directory.

## Installation

1. Create a new directory `providers` in your Keycloak installation dir (if not already existing).
2. Restart keycloak

All authenticators provided by this module are then available in the authentication flow configuration.

## Usage example

A requirement (which actually led to development of this module) was to perform a somewhat more complex - but actually quite common - conditional OTP authentication.
That is: A user shall be required to perform a multi-factor authentication (password + OTP), when he is a privileged user (= has particular keycloak role) **and** is located externally (= has foreign IP address).

There is already a "Conditional OTP Form" available in keycloak, but it fails on the required AND-condition.
Since Keycloak 10, the authentication flow is now capable of combining multiple conditional authenticators to trigger the requirement of a particular authentication module.
However, there is simply no conditional module available to match the request headers. This is where this module(s) come into play.

With that, the final authentication sub-flow for performing the conditional password + OTP authentication looks like this:

- Sub-Flow: "Conditional OTP Flow" (Type: Flow; Requirement: Conditional)
  - Execution: "Condition - User Configured" (Type: Authenticator.Conditional; Requirement: Required)
  - Execution: "Condition - User Role" (Type: Authenticator.Conditional; Requirement: Required; Configuration: User Role = xyz)
  - Execution: "Condition - Request Headers" (Type: Authenticator.Conditional; Requirement: Required; Configuration: )
  - Execution: "OTP Form" (Type: Authenticator; Requirement: Required)

Configuration for "Condition - User Role":
- User Role = xyz

Configuration for "Condition - Request Headers":
- Required Header Expression = X-Forwarded-For: (10.8.*|192.168.*)
- Expression Inversion = True

## Release History

* 1.0
    * Initial release.
    * Added ConditionalHeaderAuthenticator

## Contributing

1. Fork it (<https://github.com/jdelker/keycloak-conditional-authenticator/fork>)
2. Create your feature branch (`git checkout -b feature/fooBar`)
3. Commit your changes (`git commit -am 'Add some fooBar'`)
4. Push to the branch (`git push origin feature/fooBar`)
5. Create a new Pull Request
