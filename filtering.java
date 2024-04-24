//...
//FilmController
//...
  @GetMapping("")
	public Page<FilmDTO> findAll(@RequestParam(name = "title", required = false) String title,
												 @RequestParam(name = "description", required = false) String description,
												 Pageable pageable) {
    	logger.debug("REST : GET - findAll");
    	Page<FilmDTO> page = service.findAll(title, description, pageable);
		return page; 
    }
//...
//FilmService
//...
	public Page<FilmDTO> findAll(String title, String description, Pageable pageable) {
		logger.debug("findAll()");
		Page<FilmDTO> filmList = filmCustomRepository.queryFilmByTitleAndDescriptionPageable(
				title, description, pageable);
		return filmList;
	}
//...
//FilmCustomRepository
//...
public Page<FilmDTO> queryFilmByTitleAndDescriptionPageable(String title, String description, Pageable pageable)  {

        String queryCountStr = "select count(*) from Film F";
        String queryStr = "select F from Film F";

        if (title != null && description != null) {
            queryStr+= " where F.title like '%'||:title||'%' and F.description like '%'||:description%||'%'";
            queryCountStr+= " where F.title like '%'||:title||'%' and F.description like '%'||:description%||'%'";
        } else if (title != null) {
            queryStr+= " where lower(F.title) like '%'||:title||'%'";
            queryCountStr+= " where lower(F.title) like '%'||:title||'%'";
        } else if (description != null) {
            queryStr+= " where lower(F.description) like '%'||:description%||'%'";
            queryCountStr+= " where lower(F.description) like '%'||:description%||'%'";
        }

        Query queryCount = em.createQuery(queryCountStr);
        if (title != null && description != null) {
            queryCount.setParameter("title", title.toLowerCase());
            queryCount.setParameter("description", description.toLowerCase());
        } else if (title != null) queryCount.setParameter("title", title.toLowerCase());
        else if (description != null) queryCount.setParameter("description", description.toLowerCase());

        Long conteoTotal = (Long)queryCount.getSingleResult();

        Query query = em.createQuery(queryStr, Film.class);

        if ( pageable.getOffset() < conteoTotal ) {
            query.setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize());
        } else {
            query.setFirstResult((int) (conteoTotal -pageable.getPageSize() +1))
                    .setMaxResults(pageable.getPageSize());
        }

        if (pageable.getSort().isSorted()) {
            boolean flagNoFirst = false;
            queryStr += " order by";
            for (Sort.Order order : pageable.getSort()) {
                if (flagNoFirst) queryStr += ",";
                queryStr += " " + order.getProperty() + " " +order.getDirection();
                flagNoFirst = true;
            }
        }

        if (title != null && description != null) {
            query.setParameter("title", title.toLowerCase());
            query.setParameter("description", description.toLowerCase());
        } else if (title != null) query.setParameter("title", title.toLowerCase());
        else if (description != null) query.setParameter("description", description.toLowerCase());

        return new PageImpl<>( Util.<FilmDTO, Film>staticEntityListToDtoList(query.getResultList(), FilmDTO.class), pageable, conteoTotal);
    }
    
  //...
  //Util
  //...
    private static final ModelMapper staticMapper = new ModelMapper();

    public static <DTO, ENTITY> List<DTO> staticEntityListToDtoList(Iterable<ENTITY> entities, Class<DTO> dtoClass) {
        List<DTO> dtoList = new LinkedList<>();
        if (entities != null) {
            for (ENTITY e : entities) {
                dtoList.add(staticEntityToDto(e, dtoClass));
            }
        }
        return dtoList;
    }

    public static <DTO, ENTITY> DTO staticEntityToDto(ENTITY entity, Class<DTO> dtoClass) {
        return Util.staticMapper.map(entity, dtoClass);
        
    }

//Request
GET http://localhost:8080/api/v1/film?page=10&size=10

//Response
{
    "content": [
        {
            "filmId": 101,
            "title": "BROTHERHOOD BLANKET",
            "description": "A Fateful Character Study of a Butler And a Technical Writer who must Sink a Astronaut in Ancient Japan",
            "releaseYear": "2006-01-01",
            "languageId": 1,
            "originalLanguageId": null,
            "rentalDuration": 3,
            "rentalRate": 0.99,
            "length": 73,
            "replacementCost": 26.99,
            "rating": "R",
            "specialFeatures": "Behind the Scenes",
            "lastUpdate": "2006-02-15T05:03:42",
            "language": null,
            "language2": null,
            "filmactorList": null,
            "filmcategoryList": null,
            "inventoryList": null
        },
        {
            "filmId": 102,
            "title": "BUBBLE GROSSE",
            "description": "A Awe-Inspiring Panorama of a Crocodile And a Moose who must Confront a Girl in A Baloon",
            "releaseYear": "2006-01-01",
            "languageId": 1,
            "originalLanguageId": null,
            "rentalDuration": 4,
            "rentalRate": 4.99,
            "length": 60,
            "replacementCost": 20.99,
            "rating": "R",
            "specialFeatures": "Trailers,Commentaries,Deleted Scenes,Behind the Scenes",
            "lastUpdate": "2006-02-15T05:03:42",
            "language": null,
            "language2": null,
            "filmactorList": null,
            "filmcategoryList": null,
            "inventoryList": null
        },
        {
            "filmId": 103,
            "title": "BUCKET BROTHERHOOD",
            "description": "A Amazing Display of a Girl And a Womanizer who must Succumb a Lumberjack in A Baloon Factory",
            "releaseYear": "2006-01-01",
            "languageId": 1,
            "originalLanguageId": null,
            "rentalDuration": 7,
            "rentalRate": 4.99,
            "length": 133,
            "replacementCost": 27.99,
            "rating": "PG",
            "specialFeatures": "Commentaries,Deleted Scenes",
            "lastUpdate": "2006-02-15T05:03:42",
            "language": null,
            "language2": null,
            "filmactorList": null,
            "filmcategoryList": null,
            "inventoryList": null
        },
        {
            "filmId": 104,
            "title": "BUGSY SONG",
            "description": "A Awe-Inspiring Character Study of a Secret Agent And a Boat who must Find a Squirrel in The First Manned Space Station",
            "releaseYear": "2006-01-01",
            "languageId": 1,
            "originalLanguageId": null,
            "rentalDuration": 4,
            "rentalRate": 2.99,
            "length": 119,
            "replacementCost": 17.99,
            "rating": "G",
            "specialFeatures": "Commentaries",
            "lastUpdate": "2006-02-15T05:03:42",
            "language": null,
            "language2": null,
            "filmactorList": null,
            "filmcategoryList": null,
            "inventoryList": null
        },
        {
            "filmId": 105,
            "title": "BULL SHAWSHANK",
            "description": "A Fanciful Drama of a Moose And a Squirrel who must Conquer a Pioneer in The Canadian Rockies",
            "releaseYear": "2006-01-01",
            "languageId": 1,
            "originalLanguageId": null,
            "rentalDuration": 6,
            "rentalRate": 0.99,
            "length": 125,
            "replacementCost": 21.99,
            "rating": "NC-17",
            "specialFeatures": "Deleted Scenes",
            "lastUpdate": "2006-02-15T05:03:42",
            "language": null,
            "language2": null,
            "filmactorList": null,
            "filmcategoryList": null,
            "inventoryList": null
        },
        {
            "filmId": 106,
            "title": "BULWORTH COMMANDMENTS",
            "description": "A Amazing Display of a Mad Cow And a Pioneer who must Redeem a Sumo Wrestler in The Outback",
            "releaseYear": "2006-01-01",
            "languageId": 1,
            "originalLanguageId": null,
            "rentalDuration": 4,
            "rentalRate": 2.99,
            "length": 61,
            "replacementCost": 14.99,
            "rating": "G",
            "specialFeatures": "Trailers",
            "lastUpdate": "2006-02-15T05:03:42",
            "language": null,
            "language2": null,
            "filmactorList": null,
            "filmcategoryList": null,
            "inventoryList": null
        },
        {
            "filmId": 107,
            "title": "BUNCH MINDS",
            "description": "A Emotional Story of a Feminist And a Feminist who must Escape a Pastry Chef in A MySQL Convention",
            "releaseYear": "2006-01-01",
            "languageId": 1,
            "originalLanguageId": null,
            "rentalDuration": 4,
            "rentalRate": 2.99,
            "length": 63,
            "replacementCost": 13.99,
            "rating": "G",
            "specialFeatures": "Behind the Scenes",
            "lastUpdate": "2006-02-15T05:03:42",
            "language": null,
            "language2": null,
            "filmactorList": null,
            "filmcategoryList": null,
            "inventoryList": null
        },
        {
            "filmId": 108,
            "title": "BUTCH PANTHER",
            "description": "A Lacklusture Yarn of a Feminist And a Database Administrator who must Face a Hunter in New Orleans",
            "releaseYear": "2006-01-01",
            "languageId": 1,
            "originalLanguageId": null,
            "rentalDuration": 6,
            "rentalRate": 0.99,
            "length": 67,
            "replacementCost": 19.99,
            "rating": "PG-13",
            "specialFeatures": "Trailers,Commentaries,Deleted Scenes",
            "lastUpdate": "2006-02-15T05:03:42",
            "language": null,
            "language2": null,
            "filmactorList": null,
            "filmcategoryList": null,
            "inventoryList": null
        },
        {
            "filmId": 109,
            "title": "BUTTERFLY CHOCOLAT",
            "description": "A Fateful Story of a Girl And a Composer who must Conquer a Husband in A Shark Tank",
            "releaseYear": "2006-01-01",
            "languageId": 1,
            "originalLanguageId": null,
            "rentalDuration": 3,
            "rentalRate": 0.99,
            "length": 89,
            "replacementCost": 17.99,
            "rating": "G",
            "specialFeatures": "Behind the Scenes",
            "lastUpdate": "2006-02-15T05:03:42",
            "language": null,
            "language2": null,
            "filmactorList": null,
            "filmcategoryList": null,
            "inventoryList": null
        },
        {
            "filmId": 110,
            "title": "CABIN FLASH",
            "description": "A Stunning Epistle of a Boat And a Man who must Challenge a A Shark in A Baloon Factory",
            "releaseYear": "2006-01-01",
            "languageId": 1,
            "originalLanguageId": null,
            "rentalDuration": 4,
            "rentalRate": 0.99,
            "length": 53,
            "replacementCost": 25.99,
            "rating": "NC-17",
            "specialFeatures": "Commentaries,Deleted Scenes",
            "lastUpdate": "2006-02-15T05:03:42",
            "language": null,
            "language2": null,
            "filmactorList": null,
            "filmcategoryList": null,
            "inventoryList": null
        }
    ],
    "pageable": {
        "sort": {
            "sorted": false,
            "unsorted": true,
            "empty": true
        },
        "offset": 100,
        "pageSize": 10,
        "pageNumber": 10,
        "unpaged": false,
        "paged": true
    },
    "last": false,
    "totalPages": 100,
    "totalElements": 1000,
    "size": 10,
    "number": 10,
    "sort": {
        "sorted": false,
        "unsorted": true,
        "empty": true
    },
    "first": false,
    "numberOfElements": 10,
    "empty": false
}

//Request
GET http://localhost:8080/api/v1/film?title=di&page=3&size=10&sort=title,desc

//Response
{
    "content": [
        {
            "filmId": 454,
            "title": "IMPACT ALADDIN",
            "description": "A Epic Character Study of a Frisbee And a Moose who must Outgun a Technical Writer in A Shark Tank",
            "releaseYear": "2006-01-01",
            "languageId": 1,
            "originalLanguageId": null,
            "rentalDuration": 6,
            "rentalRate": 0.99,
            "length": 180,
            "replacementCost": 20.99,
            "rating": "PG-13",
            "specialFeatures": "Trailers,Deleted Scenes",
            "lastUpdate": "2006-02-15T05:03:42",
            "language": null,
            "language2": null,
            "filmactorList": null,
            "filmcategoryList": null,
            "inventoryList": null
        },
        {
            "filmId": 455,
            "title": "IMPOSSIBLE PREJUDICE",
            "description": "A Awe-Inspiring Yarn of a Monkey And a Hunter who must Chase a Teacher in Ancient China",
            "releaseYear": "2006-01-01",
            "languageId": 1,
            "originalLanguageId": null,
            "rentalDuration": 7,
            "rentalRate": 4.99,
            "length": 103,
            "replacementCost": 11.99,
            "rating": "NC-17",
            "specialFeatures": "Deleted Scenes",
            "lastUpdate": "2006-02-15T05:03:42",
            "language": null,
            "language2": null,
            "filmactorList": null,
            "filmcategoryList": null,
            "inventoryList": null
        },
        {
            "filmId": 458,
            "title": "INDIAN LOVE",
            "description": "A Insightful Saga of a Mad Scientist And a Mad Scientist who must Kill a Astronaut in An Abandoned Fun House",
            "releaseYear": "2006-01-01",
            "languageId": 1,
            "originalLanguageId": null,
            "rentalDuration": 4,
            "rentalRate": 0.99,
            "length": 135,
            "replacementCost": 26.99,
            "rating": "NC-17",
            "specialFeatures": "Trailers,Commentaries,Deleted Scenes,Behind the Scenes",
            "lastUpdate": "2006-02-15T05:03:42",
            "language": null,
            "language2": null,
            "filmactorList": null,
            "filmcategoryList": null,
            "inventoryList": null
        },
        {
            "filmId": 479,
            "title": "JEDI BENEATH",
            "description": "A Astounding Reflection of a Explorer And a Dentist who must Pursue a Student in Nigeria",
            "releaseYear": "2006-01-01",
            "languageId": 1,
            "originalLanguageId": null,
            "rentalDuration": 7,
            "rentalRate": 0.99,
            "length": 128,
            "replacementCost": 12.99,
            "rating": "PG",
            "specialFeatures": "Trailers,Commentaries,Deleted Scenes",
            "lastUpdate": "2006-02-15T05:03:42",
            "language": null,
            "language2": null,
            "filmactorList": null,
            "filmcategoryList": null,
            "inventoryList": null
        },
        {
            "filmId": 480,
            "title": "JEEPERS WEDDING",
            "description": "A Astounding Display of a Composer And a Dog who must Kill a Pastry Chef in Soviet Georgia",
            "releaseYear": "2006-01-01",
            "languageId": 1,
            "originalLanguageId": null,
            "rentalDuration": 3,
            "rentalRate": 2.99,
            "length": 84,
            "replacementCost": 29.99,
            "rating": "R",
            "specialFeatures": "Trailers,Commentaries,Deleted Scenes",
            "lastUpdate": "2006-02-15T05:03:42",
            "language": null,
            "language2": null,
            "filmactorList": null,
            "filmcategoryList": null,
            "inventoryList": null
        },
        {
            "filmId": 514,
            "title": "LEBOWSKI SOLDIERS",
            "description": "A Beautiful Epistle of a Secret Agent And a Pioneer who must Chase a Astronaut in Ancient China",
            "releaseYear": "2006-01-01",
            "languageId": 1,
            "originalLanguageId": null,
            "rentalDuration": 6,
            "rentalRate": 2.99,
            "length": 69,
            "replacementCost": 17.99,
            "rating": "PG-13",
            "specialFeatures": "Commentaries,Deleted Scenes",
            "lastUpdate": "2006-02-15T05:03:42",
            "language": null,
            "language2": null,
            "filmactorList": null,
            "filmcategoryList": null,
            "inventoryList": null
        },
        {
            "filmId": 516,
            "title": "LEGEND JEDI",
            "description": "A Awe-Inspiring Epistle of a Pioneer And a Student who must Outgun a Crocodile in The Outback",
            "releaseYear": "2006-01-01",
            "languageId": 1,
            "originalLanguageId": null,
            "rentalDuration": 7,
            "rentalRate": 0.99,
            "length": 59,
            "replacementCost": 18.99,
            "rating": "PG",
            "specialFeatures": "Commentaries,Deleted Scenes",
            "lastUpdate": "2006-02-15T05:03:42",
            "language": null,
            "language2": null,
            "filmactorList": null,
            "filmcategoryList": null,
            "inventoryList": null
        },
        {
            "filmId": 543,
            "title": "MADIGAN DORADO",
            "description": "A Astounding Character Study of a A Shark And a A Shark who must Discover a Crocodile in The Outback",
            "releaseYear": "2006-01-01",
            "languageId": 1,
            "originalLanguageId": null,
            "rentalDuration": 5,
            "rentalRate": 4.99,
            "length": 116,
            "replacementCost": 20.99,
            "rating": "R",
            "specialFeatures": "Deleted Scenes,Behind the Scenes",
            "lastUpdate": "2006-02-15T05:03:42",
            "language": null,
            "language2": null,
            "filmactorList": null,
            "filmcategoryList": null,
            "inventoryList": null
        },
        {
            "filmId": 544,
            "title": "MADISON TRAP",
            "description": "A Awe-Inspiring Reflection of a Monkey And a Dentist who must Overcome a Pioneer in A U-Boat",
            "releaseYear": "2006-01-01",
            "languageId": 1,
            "originalLanguageId": null,
            "rentalDuration": 4,
            "rentalRate": 2.99,
            "length": 147,
            "replacementCost": 11.99,
            "rating": "R",
            "specialFeatures": "Commentaries,Deleted Scenes,Behind the Scenes",
            "lastUpdate": "2006-02-15T05:03:42",
            "language": null,
            "language2": null,
            "filmactorList": null,
            "filmcategoryList": null,
            "inventoryList": null
        },
        {
            "filmId": 573,
            "title": "MICROCOSMOS PARADISE",
            "description": "A Touching Character Study of a Boat And a Student who must Sink a A Shark in Nigeria",
            "releaseYear": "2006-01-01",
            "languageId": 1,
            "originalLanguageId": null,
            "rentalDuration": 6,
            "rentalRate": 2.99,
            "length": 105,
            "replacementCost": 22.99,
            "rating": "PG-13",
            "specialFeatures": "Commentaries",
            "lastUpdate": "2006-02-15T05:03:42",
            "language": null,
            "language2": null,
            "filmactorList": null,
            "filmcategoryList": null,
            "inventoryList": null
        }
    ],
    "pageable": {
        "sort": {
            "sorted": true,
            "unsorted": false,
            "empty": false
        },
        "offset": 30,
        "pageNumber": 3,
        "pageSize": 10,
        "paged": true,
        "unpaged": false
    },
    "last": false,
    "totalElements": 57,
    "totalPages": 6,
    "size": 10,
    "number": 3,
    "sort": {
        "sorted": true,
        "unsorted": false,
        "empty": false
    },
    "first": false,
    "numberOfElements": 10,
    "empty": false
}
