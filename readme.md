# Sprwozdania API

### Logowanie i Rejestracja

#### POST /user/register
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

#### POST /user/login
podajesz obiekt json
````
{
    "email": "asd@qwe.pl",
    "password": "zaq1@WSX"
}
````
zwraca JSON token JWT 

#### GET /whoami
nic nie podajesz, tylko token JWT w headrze
````
Authorization: Bearer <token>
````
zwraca JSON z danymi zalogowanego usera

### Kursy

#### GET /courses
nie podajesz nic

zwaca JSON ze wszystkimi kursami

#### GET /courses/{id}
podajesz {id} w URI

zwaca JSON z jednym kursem

#### POST /courses
podajesz objekt JSON
````
{
    "name": "Narzędzia programistyczne",
    "start_time": "1970-01-01 00:00",
    "end_time": "1970-01-01 23:59"
}
````
zwraca JSON "Added new course"

#### PATCH /courses/{id}
podajesz id w URI i JSON ze zmianami
````
{
    "name": "Nowa nazwa"
}
````
zwraca JSON "updated'

#### DELETE /courses/{id}
podajesz w URI id kursu do usnięcia

zwraca JSON "deleted"

#### POST /courses/join/{access_key}
podajesz {access_key} kursu do którego chcesz dołączyć,
zwraca JSON "joined"

#### GET /courses/my
nic nie podajesz 
         
zwraca JSON z kursami zalogowanego usera

### Definicje sprawozdań

#### GET /courses/definitions 
nie podajesz nic

zwraca JSON ze wszystkimi definicjami

#### POST /courses/definitions
podajesz JSON z nową definicją, możesz dodawać tylko z rolą TEACHER, tylko do swoich kursów
````
{
    "course_id": 2,
    "title": "Obsługa GIT-a",
    "start_time": "1970-01-01 00:00",
    "end_time": "1970-01-01 23:59"
}
````
zwraca JSON "added"

#### GET /courses/{id}/definitions 
podajesz w URI {id} kursu

zwraca JSON ze wszystkimi definicjami tego kursu

#### GET /courses/definitions/{id} 
podajesz w URI {id} definicji

zwraca JSON z tą definicją

#### PATCH /courses/definitions/{id} 
podajesz JSON z tym co chcesz zmienić i {id} definicji w URI, jak przy dodawaniu można edytować tyko swoje definicje
````
{
    "start_time": "1970-01-01 04:20",
}
````
zwraca JSON "updated"

#### DELETE /courses/definitions/{id} 
podajesz {id} definicji w URI, możesz usuwać tylko swoje definicje

zwraca JSON "deleted"

## P.S. wszystkie daty podajemy w formacie
````
    yyyy-MM-dd HH:mm
    1970-01-01 00:00
````
