#security-stateless-samples

Spring security stateless restful samples



####What's feature?
Stateless restful architecture on based spring-security


###Requirements
- jdk1.8
- spring 4.1+
- spring-security 3.2+
- spring-data-mongodb 1.6+
- ehcache 2.9+
- hibernate 4.3+
- groovy 2.3+


###How-to
First,run test package ServiceTest.seed() method seed data.

Post `"/authorizations"` with `uid` and `passwd` value to get `X-AUTH-TOKEN` header

Get `"/users/id"` with `X-AUTH-TOKEN` header to get user info

:)

##Contributing
[Pull requests][] are welcome

##License 
[Apache License][].

[Pull requests]: https://help.github.com/articles/using-pull-requests "Pull requests"

[Apache License]: http://www.apache.org/licenses/LICENSE-2.0 "Apache License, Version 2.0"