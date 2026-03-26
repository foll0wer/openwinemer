# 🍷 Android Wine Cellar App (Offline, Material You)

[🇺🇸 / 🇬🇧](README.md) [🇫🇷](README.fr.md)

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
<details>
    - Wine name  
    - Producer / Estate  
    - Cuvée  
    - Vintage  
    - Wine type (sparkling, sweet, dry, semi‑dry, dessert, liqueur, other)  
    - Color (red, white, rosé, other)
</details>

### 🌍 Origin
<details>
    - Country  
    - Wine region  
    - Sub‑region  
    - Appellation  
    - Classification (Grand Cru, Premier Cru…)
</details>

### 🍇 Grapes
<details>
    - Main grape variety  
    - Blend  
    - Percentage of each grape
</details>

### ⚗️ Technical Characteristics
<details>
    - Alcohol content (%)  
    - Residual sugar  
    - Acidity  
    - pH  
    - Bottle volume  
    - Cork type  
    - Serving temperature
</details>

### 🛢️ Vinification & Aging
<details>
    - Vinification method  
    - Fermentation type  
    - Aging duration  
    - Barrel type  
    - Time in oak
</details>

### 👁️ Tasting
<details>
    - Visual appearance  
    - Aromas  
    - Flavors  
    - Structure (tannins, acidity, body)  
    - Finish  
    - Overall rating
</details>

### 🍽️ Pairings & Context
<details>
    - Recommended dishes  
    - Cuisine type  
    - Occasions  
    - Aging potential  
    - Optimal drinking date  
    - Label condition  
    - Awards / medals  
    - Recognized critics
</details>

### 💶 Commercial Information
<details>
    - Price  
    - Availability  
    - Distributor / wine shop  
    - Product code / SKU  
    - Barcode  
    - Stock quantity  
    - Cellar location  
    - Purchase date  
    - Purchase price
</details>

### 📝 Miscellaneous
<details>
    - General description
</details>

---

## 🧱 Technical Architecture
- **Kotlin** + **Jetpack Compose** (Material You)
- Local database using **Room** (offline)
- Multi‑screen Compose Navigation
- CSV/Excel export via dedicated libraries
- **MVVM** architecture
- Kotlin JVM Target 11 | Compile SDK Version 36 | Minimin SDK 26

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

## 🚧 To do later
- ✨ With a wave of pitbull's magic wand, the following information is updated via the project's [Issues](https://github.com/foll0wer/openwinemer/issues?q=is%3Aissue%20sort%3Aupdated-desc).
- 💡 Click on one of the cells to see the progress.
- 🤝 Well, for now I don’t know when I’ll work on it, and I know I only have a whopping 0 followers, but if there’s demand, I might look into it.

<div align="center">
  <a href="https://github.com/foll0wer/openwinemer/issues?q=state%3Aopen"><img src="https://img.shields.io/badge/issues-open-blue"/></a>
  <a href="https://github.com/foll0wer/openwinemer/issues?q=%20label%3Atodo"><img src="https://github.com/foll0wer/openwinemer/blob/main/.github/generated_svgs/status_status_todo.svg"/></a>
  <a href="https://github.com/foll0wer/openwinemer/issues?q=%20label%3Adoing"><img src="https://github.com/foll0wer/openwinemer/blob/main/.github/generated_svgs/status_status_doing.svg"/></a>
  <a href="https://github.com/foll0wer/openwinemer/issues?q=%20label%3Areview"><img src="https://github.com/foll0wer/openwinemer/blob/main/.github/generated_svgs/status_status_review.svg"/></a>
  <a href="https://github.com/foll0wer/openwinemer/issues?q=%20label%3Adone"><img src="https://github.com/foll0wer/openwinemer/blob/main/.github/generated_svgs/status_status_done.svg"/></a>
</div>

<div align="center">
  <a href="https://raw.githubusercontent.com/foll0wer/openwinemer/refs/heads/main/issue_0.svg">
    <img src="https://github.com/foll0wer/openwinemer/blob/main/.github/generated_svgs/issue_0.svg"/>
  </a>
</div>

<div align="center">
  <a href="https://raw.githubusercontent.com/foll0wer/openwinemer/refs/heads/main/issue_1.svg">
    <img src="https://github.com/foll0wer/openwinemer/blob/main/.github/generated_svgs/issue_1.svg"/>
  </a>
</div>

<div align="center">
  <a href="https://raw.githubusercontent.com/foll0wer/openwinemer/refs/heads/main/issue_2.svg">
    <img src="https://github.com/foll0wer/openwinemer/blob/main/.github/generated_svgs/issue_2.svg"/>
  </a>
</div>

<div align="center">
  <a href="https://raw.githubusercontent.com/foll0wer/openwinemer/refs/heads/main/issue_3.svg">
    <img src="https://github.com/foll0wer/openwinemer/blob/main/.github/generated_svgs/issue_3.svg"/>
  </a>
</div>

<div align="center">
  <a href="https://raw.githubusercontent.com/foll0wer/openwinemer/refs/heads/main/issue_4.svg">
    <img src="https://github.com/foll0wer/openwinemer/blob/main/.github/generated_svgs/issue_4.svg"/>
  </a>
</div>

<div align="center">
  <a href="https://raw.githubusercontent.com/foll0wer/openwinemer/refs/heads/main/issue_5.svg">
    <img src="https://github.com/foll0wer/openwinemer/blob/main/.github/generated_svgs/issue_5.svg"/>
  </a>
</div>

<div align="center">
  <a href="https://github.com/foll0wer/openwinemer/issues?q=is%3Aissue+sort%3Aupdated-desc">
    <img src="https://github.com/user-attachments/assets/e3deab96-be62-42b4-886b-a8509ffe761a"/>
  </a>
</div>

---

## Use of AI
AI was used in this project, mainly for error resolution (first Kotlin project for me, I know nothing about it) and for docstrings.  
Estimated carbon footprint: 200g.

### Icon
[Wine icon created by Smashicons - Flaticon.](https://www.flaticon.com/free-icons/wine).

