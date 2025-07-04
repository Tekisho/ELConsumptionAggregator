# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).
 
## [1.0.0] - 2025-03-07

First official release

## [1.0.1] - 2025-04-07

### Added

- Properly introduced structured changelog :D
- More information for MANIFEST.MD & app.properties inside pom.xml
- Author label inside "About Application" window
- "external-app-icon.ico" for installer & gen. project icon

### Fixed
- Windows installer now correctly adds a desktop shortcut via --win-shortcut (it wasnt working while --win-menu-prompt was active)

### Changed

- ${application.} properties now used for MANIFEST.MD to "sync" it with project properties
- refactored modal controller (now loading and syncing properties with UI are separated and logged)
- about-icon & (internal) app-icon now light-grey to match the overall visual style

### Removed

- "test-app-icon.png" no need