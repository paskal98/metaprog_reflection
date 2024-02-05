# Simple ORM Library
## Overview
This project introduces a simple Object-Relational Mapping (ORM) library designed to simplify the persistence layer in Java applications. By bridging the gap between Java objects and database tables, this library enables developers to perform database operations without the need for intricate SQL queries, focusing instead on object-oriented principles.

## Features
- **Reflective Persistence Management:** Utilizes a ReflectivePersistenceManager class that implements a PersistenceManager interface for managing database interactions.
- **Automatic Table Creation:** Supports the creation of database tables directly from annotated Java classes, ensuring a seamless mapping between objects and their relational counterparts.
- **CRUD Operations:** Offers comprehensive support for create, read, update, and delete (CRUD) operations, allowing for efficient data management.
- **Simplified Data Access:** Facilitates easy access to individual entities or collections based on type, ID, or column values, streamlining the process of data retrieval and manipulation.
- **Transparent Persistence:** Employs a straightforward approach to entity persistence, distinguishing between new entities (for insertion) and existing ones (for updates) based on primary key values.
## Entity Classes
Entities are simple data classes annotated to represent database tables, with support for basic data types (int, double, String) and entity relationships. Primary keys are automatically managed, ensuring unique identification for each persisted object.

## Database Compatibility
Designed with flexibility in mind, the library is compatible with SQLite, leveraging its dynamic typing system. It supports essential data types like INTEGER, REAL, and TEXT, and manages relationships through foreign keys.
