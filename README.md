# 🍷 Application Android de Cave à Vin (Offline, Material You)

<details>
    <summary> Table of contents</summary>

- [🍷 Application Android de Cave à Vin (Offline, Material You)](#-application-android-de-cave-à-vin-offline-material-you)
  - [📱 Présentation du projet](#-présentation-du-projet)
  - [🎨 Fonctionnalités principales](#-fonctionnalités-principales)
    - [🧭 Navigation et ergonomie](#-navigation-et-ergonomie)
    - [🍾 Gestion des vins](#-gestion-des-vins)
  - [🗂️ Champs disponibles pour chaque vin](#️-champs-disponibles-pour-chaque-vin)
    - [🏷️ Informations générales](#️-informations-générales)
    - [🌍 Origine](#-origine)
    - [🍇 Cépages](#-cépages)
    - [⚗️ Caractéristiques techniques](#️-caractéristiques-techniques)
    - [🛢️ Vinification \& élevage](#️-vinification--élevage)
    - [👁️ Dégustation](#️-dégustation)
    - [🍽️ Accords \& contexte](#️-accords--contexte)
    - [💶 Informations commerciales](#-informations-commerciales)
    - [📝 Divers](#-divers)
  - [🧱 Architecture technique](#-architecture-technique)
  - [🧪 Tests](#-tests)
  - [📦 Export \& sauvegarde](#-export--sauvegarde)
  - [🧰 Installation](#-installation)
  - [📄 Licence](#-licence)
  - [🚧 A faire plus tard](#-a-faire-plus-tard)
    - [Fonctions](#fonctions)
    - [Visuel](#visuel)
  - [Usage de l'IA](#usage-de-lia)
</details>

## 📱 Présentation du projet
Cette application Android permet de gérer une cave à vin complète **entièrement hors ligne**, avec une interface moderne respectant les principes du **Material You**.  
Elle offre une gestion avancée des vins, un système de tri puissant, une modification rapide des stocks et la possibilité d’exporter la base de données.

L’objectif est de fournir une application élégante, rapide, intuitive et totalement personnalisable.

---

## 🎨 Fonctionnalités principales

### 🧭 Navigation et ergonomie
- Interface Material You (thèmes dynamiques, couleurs adaptatives)
- Ajout d’un vin via un formulaire multi‑pages (un groupement de champs = une page)
- Menu de tri permettant de classer les vins selon **n’importe quel champ**

### 🍾 Gestion des vins
- Ajout, modification et suppression de chaque vin
- Modification rapide du stock via boutons **+ / -**
- Export de la base de données en **CSV** ou **Excel**
- Tous les champs sont **facultatifs** lors de l’ajout d’un vin

---

## 🗂️ Champs disponibles pour chaque vin

### 🏷️ Informations générales
<details>
    
- Nom du vin  
- Producteur / Domaine  
- Cuvée  
- Millésime  
- Type de vin (effervescent, liquoreux, sec, demi-sec, doux, liqueur, autre)  
- Couleur (rouge, blanc, rosé, autre)
</details>

### 🌍 Origine
<details>
    
- Pays  
- Région viticole  
- Sous-région  
- Appellation  
- Classement (Grand Cru, Premier Cru…)
</details>

### 🍇 Cépages
<details>
    
- Cépage principal  
- Assemblage  
- Pourcentage de chaque cépage
</details>

### ⚗️ Caractéristiques techniques
<details>
    
- Teneur en alcool (%)  
- Sucre résiduel  
- Acidité  
- pH  
- Volume de la bouteille  
- Type de bouchon  
- Température de service
</details>

### 🛢️ Vinification & élevage
<details>
    
- Méthode de vinification  
- Type de fermentation  
- Durée d’élevage  
- Type de fût  
- Temps en barrique
</details>

### 👁️ Dégustation
<details>
    
- Aspect visuel  
- Arômes  
- Saveurs  
- Structure (tanins, acidité, corps)  
- Finale  
- Note globale
</details>

### 🍽️ Accords & contexte
<details>
    
- Plats recommandés  
- Type de cuisine  
- Occasions  
- Potentiel de garde  
- Date optimale de consommation  
- État de l’étiquette  
- Récompenses / médailles  
- Critiques reconnues
</details>

### 💶 Informations commerciales
<details>
    
- Prix  
- Disponibilité  
- Distributeur / caviste  
- Code produit / SKU  
- Code-barres  
- Quantité en stock  
- Emplacement dans la cave  
- Date d’achat  
- Prix d’achat
</details>

### 📝 Divers
<details>
    
- Description générale
</details>

---

## 🧱 Architecture technique
- **Kotlin** + **Jetpack Compose** (Material You)
- Base de données locale **Room** (offline)
- Navigation Compose multi-écrans
- Export CSV/Excel via librairies dédiées
- Architecture **MVVM**

---

## 🧪 Tests
- Tests unitaires (ViewModels, Repository)  
- Tests UI (Compose Testing)

---

## 📦 Export & sauvegarde
L’utilisateur peut exporter sa base de données complète en :  
- **CSV** (compatible Excel, LibreOffice…)  
- **XLSX** (Excel natif)
- **Backup local** pour réimporter sur l'app

---

## 🧰 Installation
1. Cloner le dépôt  
2. Ouvrir dans Android Studio  
3. Lancer l’application sur un appareil ou un émulateur Android 8+

---

## 📄 Licence
GNU GENERAL PUBLIC LICENSE 3

---

## 🚧 A faire plus tard
- ✨ Avec un coup de baguette wati magique les infos suivantes sont mises à jour via les [Issues](https://github.com/foll0wer/openwinemer/issues?q=is%3Aissue%20sort%3Aupdated-desc) du projet.
- 💡 Cliquer sur une des celules pour voir l'avancement.
- 🤝 Bon pour l'instant je sais pas quand je travaillerai dessus, et je sais que je n'ai que 0 follower, mais si il y a des demandes je pourrais m'y pencher.

<div align="center">
  <a href="https://github.com/foll0wer/openwinemer/issues?q=state%3Aopen"><img src="https://img.shields.io/badge/issues-open-blue"/></a>
  <a href="https://github.com/foll0wer/openwinemer/issues?q=%20label%3Atodo"><img src="https://img.shields.io/badge/issues-todo-orange"/></a>
  <a href="https://github.com/foll0wer/openwinemer/issues?q=%20label%3Adoing"><img src="https://img.shields.io/badge/issues-doing-red"/></a>
  <a href="https://github.com/foll0wer/openwinemer/issues?q=%20label%3Areview"><img src="https://img.shields.io/badge/issues-review-yellow"/></a>
  <a href="https://github.com/foll0wer/openwinemer/issues?q=%20label%3Adone"><img src="https://img.shields.io/badge/issues-done-purple"/></a>
</div>

---

## Usage de l'IA
L'IA a été utilisé pour ce projet, notamment pour la résolution des erreurs (premier projet en kotlin pour moi, j'y connais rien) et les docstrings.
Estimation d'empreinte carbone : 200g.

### Icon
[Wine icon created by Smashicons - Flaticon.](https://www.flaticon.com/free-icons/wine).

Test

<div align="center">
  <img src="https://raw.githubusercontent.com/foll0wer/openwinemer/.github/generated_svgs/issue_0.svg"/>
</div>

