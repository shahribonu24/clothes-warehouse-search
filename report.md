# Progress Report — Clothes and Footwear Warehouse Search System

**Author:** Shahribonu Isakova
**Course project period:** September 28, 2026 — December 27, 2026

## 1. Work Plan and Progress

| #   | Stage                  | Start date | End date   | Comment |
| --- | ---                    | ---        | ---        | ---     |
| 1   | Task clarification     | 2026-09-28 | 2026-09-28 | Subject area received: *Clothes and Footwear Warehouse*. Work plan and this report created. |
| 2   | Analysis               | 2026-09-29 | 2026-09-30 | Subject area studied, MVP / additional / advanced functionality described (section 2). |
| 3   | Use cases              | 2026-10-01 | 2026-10-02 | Use cases described (section 3). |
| 4   | Search for solutions   | 2026-10-05 | 2026-10-09 | Layered architecture chosen (section 4). Checkpoint #1: Maven project, repository, readme, .gitignore. |
| 5   | Software development   | 2026-10-12 | 2026-12-06 | Planned order: entities → DAO → service → controller → view → startup. Checkpoint #2 (entities + DAO + DAO tests), checkpoint #3 (service + controller). |
| 6   | Development completion | 2026-12-07 | 2026-12-10 | Compare result with the plan, prepare demo scenario. |
| 7   | Presentation           | 2026-12-11 | 2026-12-11 | Demonstration of the application to the instructor. |

## 2. Analysis

### Subject area

A clothes and footwear warehouse stores product *lines* (models). Every product line has an
identifier, a name, a brand, a color, a target gender, a material, a unit price and a quantity in stock.
Products are divided into two big groups that have different sizing systems:

* **Clothing** — letter sizes (XS…XXL) and a season (winter, summer, demi-season, all-season).
  * **Jackets** — can be waterproof, can have a hood.
  * **Jeans** — waist and length in inches, fit (skinny, slim, straight, regular, relaxed).
* **Footwear** — EU shoe size.
  * **Sneakers** — sport type (running, basketball, training, tennis, lifestyle).
  * **Boots** — can be waterproof, shaft height in cm.

"Waterproof" is a behaviour shared by products from *different* branches (jackets and boots),
so it is modelled as an interface, not as a class in the hierarchy.

### Basic functionality (MVP)

* Show application name, version, creation date, developer info and the menu on start.
* Read the inventory from pre-formatted text (CSV) files — one file per category.
* Search products by any combination of parameters (common and category-specific).
* Show product details (ID, category, name, brand, price, quantity, specific attributes).
* Show "no products found" when nothing matches.
* Show the whole inventory sorted by the selected field and direction.
* Graceful error handling and an exit command.

### Additional functionality

* Range parameters (`price=50;150`, `price=;100`, `quantity=1;`).
* Multiple values for enumerated parameters (`size=M,L`, `gender=MEN,UNISEX`).
* Search in groups of categories (`all`, `clothing`, `footwear`).
* Inventory statistics: product lines, units in stock, stock value, out-of-stock lines.
* Client-side (view) validation of the input syntax.
* Batch mode: execute commands from a text file.
* External data directory (`--data <dir>`), so the inventory can be replaced without rebuilding.

### Advanced functionality (future)

* Adding / editing / removing products and saving changes back to files.
* Other data sources (database, JSON) — only a new `Source` implementation is needed.
* Graphical or web user interface — only a new `View` implementation is needed.
* Orders and stock reservation, low-stock notifications.

## 3. Use Cases

| # | Use case | Action | Result |
| --- | --- | --- | --- |
| 1 | Become familiar with the program | Launch the program without parameters | Name, version, creation date, developer info and the list of commands are shown; the program waits for a command. |
| 2 | See all products | `list` or `list all sort=price order=desc` | All products are shown in the selected order; the program waits for the next command. |
| 3 | See products of one category | `list jackets` | Only jackets are shown. |
| 4 | Find products by parameters | `find clothing size=M season=WINTER price=;200` | Matching products with details are shown. |
| 5 | Find by text with spaces | `find sneakers name="Air Max"` | Products whose name contains "Air Max". |
| 6 | Nothing found | `find boots brand=Unknown` | Message "No products found matching your criteria." |
| 7 | Wrong input | `find jackets price=abc`, `fnd`, `find jackets size=M size=L` | A clear error message; the program keeps working. |
| 8 | Inapplicable parameter | `find sneakers waterproof=true` | Error: parameter is not applicable to the selected category. |
| 9 | See search parameters | `help find` | List of parameters, their format and categories they apply to. |
| 10 | See statistics | `stats footwear` | Number of lines, units and stock value per category with totals. |
| 11 | Batch processing | Launch with `--batch commands.txt` | Commands from the file are executed one by one, results are printed. |
| 12 | Exit | `exit` | Goodbye message, the program terminates. |
| 13 | Data source failure | Data file is missing or corrupted | Error message with file name and line; the program terminates gracefully. |

## 4. Chosen Architecture

Layered architecture (no frameworks), every layer depends only on the layer directly below it
and only through interfaces / factories:

```
startup     Main, ApplicationInitializer — wires all layers together
view        View, ConsoleView, BatchView, InputParser (client-side validation)
controller  Controller, Request, Response, Command pattern, parameter parsing, formatting
service     ProductService — search across categories, sorting, statistics
dao         ProductDao, Source (CSV), mappers, SearchCriteria, Parameter hierarchy
entity      Product → Clothing → Jacket / Jeans, Product → Footwear → Sneakers / Boots
```

## 5. Development Log

| Date | Work done |
| --- | --- |
| 2026-11-01 | Project skeleton, readme, .gitignore, report (checkpoint #1). |
| 2026-11-01 | Entity hierarchy: Product → Clothing/Footwear → Jacket, Jeans, Sneakers, Boots; Waterproof interface; Category enum; entity tests. |
| 2026-11-01 | DAO layer (checkpoint #2): SearchCriteria and Parameter hierarchies, CSV source, mappers, DaoFactory; test CSV files in `src/test/resources`; DAO tests. |

## 6. Next Steps

* Service layer and its tests (checkpoint #3).
* Controller layer and its tests.
* View layer, startup layer, final data files.
