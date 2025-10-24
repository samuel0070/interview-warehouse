# 🏬 Shop Inventory Management API

A Spring Boot 3.x RESTful backend for managing a shop's inventory, categories, and variants.

---

## 🚀 Features

- Manage **Items** with variants (size, color, etc.)
- Manage **Categories** with parent–child relationships
- **Track Stock** levels per variant
- **Prevent Selling** items that are out of stock
- **Pagination, Sorting, and Filtering**
- **Flyway** database migrations
- **Swagger UI** for easy API testing

---

## 🛠️ Tech Stack

- **Java 21**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **PostgreSQL**
- **Flyway**
- **Lombok**
- **Swagger (Springdoc OpenAPI)**

---

## ⚙️ Setup Instructions

### 1️⃣ Clone the Repository
git clone https://github.com/samuel0070/interview-warehouse.git

### Create DB first named test_code_shop_inventory
CREATE DATABASE test_code_shop_inventory;

### How to Run
 mvn spring-boot:run

### Design table
-using 4 tables: category, items, variants, and inventory_logs
-all the tables are dependent of each other using foreign key
-I use table category to separate between items(one to many), items also one to many relation to variants
-concept of category was parent-child (but i just made 1 level),*C1(parent_id is empty) is the parent of C2(parent_id not empty)
-I didn't make delete for this project because of on point 2, it can be harm to data
### tech architecture 
i'm using flyway, so that sql syntax like create table, or insert dummy data can be done on the fly
i'm using swagger, so tester can test it knowing the fields(json) and also exmple of return value
## example API endpoint
## ------------------------------Category----------------------------------

-----------------------------Create-------------------------------------
-------POST
categoryName = string
categoryType = string
parentId = uuid can be null
status = integer
C1(Parent)
  http://localhost:8080/categories?categoryName=Sport&categoryType=Equipment&status=1

C2(child)
    http://localhost:8080/categories?categoryName=Sport%20indoor&categoryType=Racket&parentId=cd7474dd-bf60-4485-8dc3-e9529ecec226&status=1' 

------------LIST View-----------------
---------GET
page = integer,default 0
categoryType = integer
sort = only categoryName ascending

http://localhost:8080/categories?page=0&size=1&sort=categoryName%2Casc

http://localhost:8080/categories/c1?page=0&size=1&sort=categoryName%2Casc


## ---------------------------- ITEM - VARIANT ----------------------------------------
insert item and variant, i assume that if :
1. diffrent item it could insert into table
2. it was same item(name and brand) but diffrent variant it could insert into table
3. SKU uniq and can be generate automaticlly, Brand(5 chars)+color(3 char)+material(3 char)+ size+ combination yearMonthRandom ex: YONEX-BLA-POL-L-2510296
-----Create--------
-------POST-------
{
  "name": "Speed Dry",
  "description": "new collection",
  "categoryId": "d03fcb96-d10f-41aa-97e2-d5fb3a7948a9",
  "basePrice": 180000,
  "brandName":"ASICS",
  "variants": [
    {
      "size": "L",
      "color": "Black",
      "material": "Poly",
      "price": 195000,
      "stockQuantity": 10,
      "attributes": {
        "motif": "Batik",
        "model_kerah": "0",
        "tipe": "Slimfit",
       "garansi":"30hari"
      }
    }
  ]
}

-----------Update Stock------------
-----POST-------
inventory/variants-update/stock/{variantId}
request body:
{
  "quantityChange": 200, // can be negative value
  "reason": "test update stock"
}
http://localhost:8080/inventory/variants-update/stock/024b90b4-6e31-4be8-b78f-3c8a2e977161

------SELL -------

-----POST------
variants/{variantId}/sell
http://localhost:8080/inventory/variants/773a12a7-b871-499d-946d-709d18854eca/sell?quantity=3

------LIST of stock threshold(with limit)----------

-------GET---------
http://localhost:8080/inventory/variants/low-stock?threshold=12


------LIST HISTORY INVENTORY-------

-----GET------
inventory/variants-history/{variantId}
http://localhost:8080/inventory/variants-history/49a570c5-6401-4d08-ab74-b67cb9a8f6ad'