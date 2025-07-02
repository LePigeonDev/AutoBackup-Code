# AutoBackup

**AutoBackup** est une application JavaFX développée en Java 21 permettant de lancer manuellement des sauvegardes de fichiers depuis une interface moderne et intuitive.

## 🎯 Fonctionnalités

- Sélection de plusieurs dossiers sources à sauvegarder
- Choix du dossier de destination
- Interface graphique claire et moderne (JavaFX)
- Affichage du statut, de la progression et du temps écoulé
- Réinitialisation rapide de la sélection
- Journalisation en temps réel des fichiers copiés

> ⚠️ La sauvegarde n'est **pas automatique** mais déclenchée manuellement.

---

## 🛠️ Technologies utilisées

- **Java 21**
- **JavaFX 21**
- **Maven** pour la gestion de projet
- **jpackage** pour la génération d’un exécutable `.exe` Windows

---

## 📦 Installation

### Via l'exécutable

1. Télécharge le fichier `AutoBackup.exe` depuis la [dernière release](https://github.com/TON_NOM_UTILISATEUR/AutoBackup/releases).
2. Lance l’installation.
3. Utilise le raccourci sur le bureau ou dans le menu Démarrer.

### Depuis le code source

#### Prérequis :
- Java 21 (Adoptium recommandé)
- JavaFX 21 SDK
- Maven
- jpackage (inclus dans le JDK)

```bash
git clone https://github.com/TON_NOM_UTILISATEUR/AutoBackup.git
cd AutoBackup/autobackup
mvn clean package

```
Puis, pour lancer :
```bash
java --module-path "chemin/vers/javafx-sdk-21.0.7/lib" --add-modules javafx.controls,javafx.fxml -jar target/autobackup-1.0-jar-with-dependencies.jar
```
