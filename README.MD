# Helmes Technical Assignment

_**Note:** sometimes `sectors` are also called `categories` in the code 
and documentation_

### How to Run (One of Options)
1. Run [launch_database_docker_container.sh](./java/docker/launch_database_docker_container.sh) script launching database in a Docker container
2. In the same directory there is `maven_build.sh` script - it builds
    executable JAR that can be run. Run it and then run the JAR
3. In the `angular` folder run `ng serve` to launch the Angular frontend

After everything is done, you can try accessing these endpoints:
- `http://localhost:8080/form` - frontend implementation with Thymeleaf 
    (implemented in Java part).
    Everything should work as expected and requested.
- `http://localhost:4200/` - frontend implementation with Angular. As it is 
not a technology I am good at, it can
- `http://localhost:8080/create` - this endpoint fills database with
    pre-defined sectors. Use it if sectors are not displaying in the frontend.



### Quote: "behind the choice should also be an explanation of why this particular solution variant was chosen"
1. **Unified Naming of Sectors**

    Some categories used to be named like
        "Other (*Parent Category Name*)" like
        `Other (Furniture)` in `Furniture` and `Other (Wood)` in `Wood`. 
        Those are the subcategories or children categories of a
        parent category. As some other children in other categories
        already were called just `Other` a decision was made to have
        just `Other`. The advantages of this decision are:
       - Simplified implementation of ordering where `Other` categories
       are the bottom of a selection list.
         - Unified naming - only one style for exceptions that cannot be
         part of pre-defined categories.

2. **Structure of Sector in Database**

    The Sector represented in database as entity with following 
    fields:
    - id (integer), not null, primary key - as max value of integer is expected to be
        sufficient to accommodate all sectors that are already
        defined or can be added in the future
    - name (VARCHAR(255))
    - parentId (integer) - foreign key to the primary key
   
   This structure allows adding new sectors and subcategories 
    without any effect on already-existing ones. IDs of sectors
    that are children can be queried on demand. Thus, there is no need
    to store them.

3. **Structure of UserData**

    UserData is a structure that stores the data that was input 
    by a user. In database, it is represented as by these structures:

    Table `user_data`:
    - id (bigint), not null, primary key
      - name (varchar(255)) - user's name
      - agree_to_terms (boolean) - did user agreed to terms
    
    Table `user_data_ids_of_involved_sectors`:
    - ids_of_involved_sectors (integer) - ID of a selected sector
      - user_data_id            (bigint) - foreign key referencing primary
          key of the `user_data` table

4. **Requirement 3.4 - "Allow the user to edit his/her own data during the session"**

    This was used by storing and ID of saved UserData in HttpSession.
    This decision makes possible for users to edit their own data during the session
    without the need to implement complicated mechanisms of login and
    registration.

    The session ends when user closes their browser or after some time
   (1800 seconds).

### Other Endpoints
There are REST endpoints:

GET /rest/categories:
- Returns all information about sectors. 
    Accepts `isDetailed` request parameter. If `?isDetailed=true`, then
    returns response containing all sectors and their children

GET /rest/categories/{categoryId}
- Returns all information about sector with provided ID.
  Accepts `isDetailed` request parameter. If `?isDetailed=true`, then
  returns response containing sector data with all children

POST /userData
- Accepts UserData object in JSON format

GET /userData/{recordId}
- Returns UserData details for requested ID