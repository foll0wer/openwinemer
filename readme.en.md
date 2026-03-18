# 🍷 Android Wine Cellar App (Offline, Material You)

<details>
    <summary> Table of contents</summary>

- [🍷 Android Wine Cellar App (Offline, Material You)](#-android-wine-cellar-app-offline-material-you)
  - [📱 Project Overview](#-project-overview)
  - [🎨 Main Features](#-main-features)
    - [🧭 Navigation \& UX](#-navigation--ux)
    - [🍾 Wine Management](#-wine-management)
  - [🗂️ Available Fields for Each Wine](#️-available-fields-for-each-wine)
    - [🏷️ General Information](#️-general-information)
    - [🌍 Origin](#-origin)
    - [🍇 Grapes](#-grapes)
    - [⚗️ Technical Characteristics](#️-technical-characteristics)
    - [🛢️ Vinification \& Aging](#️-vinification--aging)
    - [👁️ Tasting](#️-tasting)
    - [🍽️ Pairings \& Context](#️-pairings--context)
    - [💶 Commercial Information](#-commercial-information)
    - [📝 Miscellaneous](#-miscellaneous)
  - [🧱 Technical Architecture](#-technical-architecture)
  - [🚀 Future Features](#-future-features)
  - [🧪 Tests](#-tests)
  - [📦 Export \& Backup](#-export--backup)
  - [🧰 Installation](#-installation)
  - [📄 License](#-license)
  - [⏰ To‑Do Later](#-todo-later)
    - [Features](#features)
    - [Visual](#visual)
  - [Use of AI](#use-of-ai)
</details>

## 📱 Project Overview
This Android application allows you to manage a complete wine cellar **entirely offline**, with a modern interface following **Material You** principles.  
It offers advanced wine management, powerful sorting options, quick stock editing, and the ability to export the database.

The goal is to provide an elegant, fast, intuitive, and fully customizable application.

---

## 🎨 Main Features

### 🧭 Navigation & UX
- Material You interface (dynamic themes, adaptive colors)
- Add a wine through a multi‑page form (one group of fields = one page)
- Sorting menu allowing wines to be sorted by **any field**

### 🍾 Wine Management
- Add, edit, and delete wines
- Quick stock adjustment using **+ / -** buttons
- Export the database to **CSV** or **Excel**
- All fields are **optional** when adding a wine

---

## 🗂️ Available Fields for Each Wine

### 🏷️ General Information
- Wine name  
- Producer / Estate  
- Cuvée  
- Vintage  
- Wine type (sparkling, sweet, dry, semi‑dry, dessert, liqueur, other)  
- Color (red, white, rosé, other)

### 🌍 Origin
- Country  
- Wine region  
- Sub‑region  
- Appellation  
- Classification (Grand Cru, Premier Cru…)

### 🍇 Grapes
- Main grape variety  
- Blend  
- Percentage of each grape

### ⚗️ Technical Characteristics
- Alcohol content (%)  
- Residual sugar  
- Acidity  
- pH  
- Bottle volume  
- Cork type  
- Serving temperature

### 🛢️ Vinification & Aging
- Vinification method  
- Fermentation type  
- Aging duration  
- Barrel type  
- Time in oak

### 👁️ Tasting
- Visual appearance  
- Aromas  
- Flavors  
- Structure (tannins, acidity, body)  
- Finish  
- Overall rating

### 🍽️ Pairings & Context
- Recommended dishes  
- Cuisine type  
- Occasions  
- Aging potential  
- Optimal drinking date  
- Label condition  
- Awards / medals  
- Recognized critics

### 💶 Commercial Information
- Price  
- Availability  
- Distributor / wine shop  
- Product code / SKU  
- Barcode  
- Stock quantity  
- Cellar location  
- Purchase date  
- Purchase price

### 📝 Miscellaneous
- General description

---

## 🧱 Technical Architecture
- **Kotlin** + **Jetpack Compose** (Material You)
- Local database using **Room** (offline)
- Multi‑screen Compose Navigation
- CSV/Excel export via dedicated libraries
- **MVVM** architecture

---

## 🚀 Future Features
- Advanced search  
- Statistics (distribution by country, color, vintage…)  
- Optimized tablet mode  
- Encrypted local backup  

---

## 🧪 Tests
- Unit tests (ViewModels, Repository)  
- UI tests (Compose Testing)

---

## 📦 Export & Backup
The user can export the complete database as:  
- **CSV** (Excel, LibreOffice compatible)  
- **XLSX** (native Excel)  
- **Local backup** to re‑import into the app

---

## 🧰 Installation
1. Clone the repository  
2. Open in Android Studio  
3. Run the application on an Android 8+ device or emulator  

---

## 📄 License
GNU GENERAL PUBLIC LICENSE 3

---

## ⏰ To‑Do Later
### Features
- Add wine price tracking  
- Add an option to share X wines
- Translate to english and add translation module

### Visual
- Add a graph for price tracking  
- Add a “tutorial bubble” explaining how the app works on first launch

---

## Use of AI
AI was used in this project, mainly for error resolution (first Kotlin project for me, I know nothing about it) and for docstrings.  
Estimated carbon footprint: 200g.
