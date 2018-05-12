# Sprwozdania API
## Logowanie i Rejestracja

### POST /user/register/json
podajesz obiekt json
````
{
    "email": "asd@qwe.pl",
    "name": "Janusz",
    "last_name": "Pawlacz",
    "password": "zaq1@WSX"
}
````
zwraca JSON "registered"

### POST /user/login/json
podajesz obiekt json
````
{
    "email": "asd@qwe.pl",
    "password": "zaq1@WSX"
}
````
zwraca JSON token JWT 

### GET /all
nic nie podajesz, tylko token JWT w headrze
````
Authorization: Bearer <token>
````
zwraca JSON z wszyskimi userami

