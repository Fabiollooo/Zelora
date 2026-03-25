# Zelora

A comprehensive e-commerce management system for clothing retailers with customer profiles, product inventory, orders, and personalized recommendations.

## Overview

Zelora is a Java-based desktop application that manages a complete retail operation, including customer registration and profiles, product catalog with categories, inventory tracking, order processing, and customer wishlists. The system tracks customer VIP status, preferences, and communication channels, while maintaining real-time stock alerts and order history.

## Tech Stack

- **Language**: Java 18
- **Build Tool**: Maven
- **Database**: MySQL / MariaDB
- **Key Libraries**:
  - Lombok (boilerplate reduction)
  - MySQL Connector (database connectivity)
  - Apache Commons DBUtils (query execution)
  - BCrypt (password encryption)
  - JavaFaker (test data generation)
  - OpenCSV (CSV import/export)

## Quick Start

### Prerequisites
- IntelliJ IDEA (Community or Ultimate edition)
- Java 18 or higher
- MySQL/MariaDB running

### Setup Instructions

1. **Open project in IntelliJ**:
   - Launch IntelliJ IDEA
   - Click "File" → "Open"
   - Select the `Zelora-main` folder and click "Open as Project"
   - Wait for Maven to index and download dependencies

2. **Create database** - Import the SQL file to create the database and tables:
   - Open a MySQL client (command line or MySQL Workbench)
   - Run: `source src/database_export/zelora.sql;`

3. **Run the application**:
   - Locate the `main` method in `src/main/java/zelora/app/ZeloraApp.java`
   - Right-click on the file and select "Run 'ZeloraApp.main()'"
   - Or press `Ctrl+Shift+F10` (Windows/Linux) or `Ctrl+Shift+R` (Mac)

## Features

- **Customer Management**: Register customers, manage profiles, track VIP status (Bronze/Silver/Gold/Platinum)
- **Product Catalog**: Organize products by categories (Men's, Women's, Shoes, etc.)
- **Inventory Tracking**: Monitor stock levels, reorder points, and supplier assignments
- **Order Processing**: Create and manage customer orders with line items
- **Wishlist**: Customers can save products for later
- **Real-Time Alerts**: Low stock warnings and new customer notifications
- **Reviews & Ratings**: Customer feedback system for products
- **CSV Import/Export**: Bulk product data handling
- **Data Validation**: Secure authentication with BCrypt password hashing

## Project Structure

```
src/main/java/zelora/
├── app/                    # Main application entry point
├── data/                   # Database connection and operations
├── model/                  # Entity classes (Customer, Product, Order, etc.)
└── util/                   # Utility classes and banner display

src/database_export/
└── zelora.sql             # Complete database schema and sample data

src/main/resources/
└── products.csv           # Sample product data
```

## Database Tables

| Table | Purpose |
|-------|---------|
| `customers` | Customer profiles, contact info, preferences, VIP status |
| `products` | Product catalog with pricing and descriptions |
| `categories` | Product categories |
| `inventory` | Stock levels, reorder points, supplier assignments |
| `orders` | Customer orders with dates and statuses |
| `order_items` | Individual line items within orders |
| `reviews` | Product reviews and ratings |
| `wishlist` | Products customers save for later |
| `suppliers` | Supplier information and contacts |

## Known Issues

- Default XAMPP setup assumes blank MySQL password
- Product images default to "no-image.jpg" placeholder
- Some legacy test customer data included in database dump

## Future Improvements

- Web-based interface (replacing CLI)
- Advanced reporting and analytics dashboard
- Customer segmentation and targeted marketing
- Automated discount/promotion engine
- Integration with payment gateways
- Advanced search and filtering features
- Product recommendation engine improvements
- Audit logging for all transactions

