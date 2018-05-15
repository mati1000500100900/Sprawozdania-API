# Sprwozdania API
## Logowanie i Rejestracja

### POST /user/register
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

### POST /user/login
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

## Kursy

### GET /courses
nie podajesz nic
zwaca JSON ze wszystkimi kursami

### GET /courses/{id}
nie podajesz nic, poza id w URI
zwaca JSON z jednym kursem

### POST /courses
podajesz objekt JSON
````
{
    "name": "Narzędzia programistyczne",
    "start_time": "1970-01-01 00:00",
    "end_time": "1970-01-01 23:59"
}
````
zwraca JSON "Added new course"

### PATCH /courses/{id}
podajesz id w URI i JSON ze zmianami
````
{
    "name": "Nowa nazwa"
}
````
zwraca JSON Updated


## P.S. wszystkie daty podajemy w formacie
````
    yyyy-MM-dd HH:mm
    1970-01-01 00:00
````
