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
  - [🚀 Fonctionnalités futures](#-fonctionnalités-futures)
  - [🧪 Tests](#-tests)
  - [📦 Export \& sauvegarde](#-export--sauvegarde)
  - [🧰 Installation](#-installation)
  - [📄 Licence](#-licence)
  - [⏰ A faire plus tard](#-a-faire-plus-tard)
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
- Nom du vin  
- Producteur / Domaine  
- Cuvée  
- Millésime  
- Type de vin (effervescent, liquoreux, sec, demi-sec, doux, liqueur, autre)  
- Couleur (rouge, blanc, rosé, autre)

### 🌍 Origine
- Pays  
- Région viticole  
- Sous-région  
- Appellation  
- Classement (Grand Cru, Premier Cru…)

### 🍇 Cépages
- Cépage principal  
- Assemblage  
- Pourcentage de chaque cépage

### ⚗️ Caractéristiques techniques
- Teneur en alcool (%)  
- Sucre résiduel  
- Acidité  
- pH  
- Volume de la bouteille  
- Type de bouchon  
- Température de service

### 🛢️ Vinification & élevage
- Méthode de vinification  
- Type de fermentation  
- Durée d’élevage  
- Type de fût  
- Temps en barrique

### 👁️ Dégustation
- Aspect visuel  
- Arômes  
- Saveurs  
- Structure (tanins, acidité, corps)  
- Finale  
- Note globale

### 🍽️ Accords & contexte
- Plats recommandés  
- Type de cuisine  
- Occasions  
- Potentiel de garde  
- Date optimale de consommation  
- État de l’étiquette  
- Récompenses / médailles  
- Critiques reconnues

### 💶 Informations commerciales
- Prix  
- Disponibilité  
- Distributeur / caviste  
- Code produit / SKU  
- Code-barres  
- Quantité en stock  
- Emplacement dans la cave  
- Date d’achat  
- Prix d’achat

### 📝 Divers
- Description générale

---

## 🧱 Architecture technique
- **Kotlin** + **Jetpack Compose** (Material You)
- Base de données locale **Room** (offline)
- Navigation Compose multi-écrans
- Export CSV/Excel via librairies dédiées
- Architecture **MVVM**

---

## 🚀 Fonctionnalités futures
- Recherche avancée  
- Statistiques (répartition par pays, couleurs, millésimes…)  
- Mode tablette optimisé  
- Sauvegarde locale chiffrée  

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

## ⏰ A faire plus tard
### Fonctions
- Incorporer un suivi des prix des vins ;
- Ajouter une option pour partager X vins ;
- Traduction anglaise et implémentation des changements de langue ;
- Finir l'export csv.

### Visuel
- Ajouter un graph pour le suivi des prix ;
- Ajouter bulle de dialogue "tuto" qui explique comment marche l'appli au premier démarrage.

---

## Usage de l'IA
L'IA a été utilisé pour ce projet, notamment pour la résolution des erreurs (premier projet en kotlin pour moi, j'y connais rien) et les docstrings.
Estimation d'empreinte carbone : 200g.

### Icon
[Wine icon created by Smashicons - Flaticon.](https://www.flaticon.com/free-icons/wine).