# Clothes and Footwear Warehouse Search System

**Author:** Shahribonu Isakova

A Java console application that lets a user search the inventory of a clothes and footwear
warehouse by any combination of parameters (name, brand, size, price range, season, etc.).
Inventory data is read from pre-formatted CSV text files.

Course project of the EPAM Java training (individual subject area: *Clothes and Footwear Warehouse*).

## Current status

* Entity layer — done (checkpoint #2)
* DAO layer (CSV source, search criteria, parameters) — done (checkpoint #2)
* Service layer (search across categories, sorting, top/bottom k, statistics, cost) — done (checkpoint #3)
* Controller layer (request parsing, Command pattern, formatting) — done (checkpoint #3)
* Unit and integration tests for all layers — `mvn test`

View and startup layers are in progress.

## Commands (controller layer)

```
help [command]
list [target] [sort=<field>] [order=asc|desc] [top=k|bottom=k]
find <target> [parameter=value ...] [sort=<field>] [order=asc|desc] [top=k|bottom=k]
stats [target] [parameter=value ...]
cost [target] [parameter=value ...]
exit
```

* Targets: `all`, `clothing`, `footwear`, `jackets`, `jeans`, `sneakers`, `boots`.
* Sort fields: `id`, `name`, `brand`, `category`, `price`, `quantity`, `value` (price × quantity).
* `top=k` / `bottom=k` — only the first / last k products of the sorted list.
* Ranges: `price=50;150`, `price=;100`, `quantity=1;`; several values: `size=M,L`; spaces: `name="Air Max"`.

Examples:

```
find clothing size=M,L season=WINTER price=;200
find all sort=price order=desc top=3
cost footwear brand=Nike
stats clothing
```
