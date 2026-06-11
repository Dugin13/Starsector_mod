# Bribe AI Inspectors with Credits

A Starsector mod that replaces the story point cost for bribing Hegemony AI inspectors with a credit-based system.

## Features

- **No more story points needed** to bribe AI inspectors
- **Dynamic pricing** based on:
  - Fleet size (fleet points)
  - Cargo value
  - Base cost of 250,000 credits
- **Maximum cap**: 1,000,000 credits

## Requirements

- Starsector 0.97a-RC11 or compatible version
- Java Development Kit (JDK) 8 for building

## Installation

1. Copy the `bribe_with_credits` folder to your Starsector mods directory
2. Enable the mod in the Starsector launcher
3. Start a new game or load an existing save

## Building from Source

### Prerequisites

- JDK 8 installed
- Starsector installed (default path: `C:\Program Files (x86)\Fractal Softworks\Starsector`)

### Build Instructions

1. Open PowerShell in the mod directory
2. Edit `build.ps1` to point to your Starsector installation if different
3. Run: `.\build.ps1`
4. The JAR file will be created in the `jars/` directory

### Manual Build

```powershell
# Set Starsector path
$STARSECTOR = "C:\Program Files (x86)\Fractal Softworks\Starsector"

# Compile
javac -cp "$STARSECTOR\starsector-core\starfarer.api.jar;$STARSECTOR\starsector-core\starfarer_obf.jar" `
      -d out -source 8 -target 8 src/com/bribecredits/*.java

# Create JAR
cd out
jar cf ../jars/BribeWithCredits.jar *
cd ..
```

## How It Works

The mod intercepts Hegemony AI inspection encounters and adds a credit-based bribe option alongside or instead of the story point option.

**Bribe cost formula:**
```
Base Cost: 250,000 credits
+ (Fleet Points × 1,000)
+ (Cargo Space Used × 5,000)
= Total (capped at 1,000,000)
```

## Files Structure

```
bribe_with_credits/
├── mod_info.json                    # Mod metadata
├── data/
│   └── campaign/
│       └── rules.csv                # Dialog rules for bribe option
├── src/
│   └── com/
│       └── bribecredits/
│           ├── BribeCreditsPlugin.java         # Main mod plugin
│           ├── AIInspectorInterceptor.java     # Detects AI inspections
│           └── BribeCreditCommand.java         # Handles bribe logic
├── jars/
│   └── BribeWithCredits.jar         # Compiled code (generated)
└── build.ps1                        # Build script
```

## Customization

To adjust bribe costs, edit `AIInspectorInterceptor.java`:

```java
private static final float BRIBE_COST_BASE = 250000f;  // Base cost
cost += fleetPoints * 1000;                            // Per fleet point
cost += cargoValue;                                     // Cargo multiplier
```

Then rebuild the JAR.

## Troubleshooting

**Mod not loading:**
- Check `mod_info.json` has correct `jars` path
- Verify JAR file exists in `jars/` directory
- Check Starsector's `starsector.log` for errors

**Bribe option not appearing:**
- This is a complex mod that hooks into internal game systems
- May conflict with other mods that modify AI inspections
- Check the rules.csv integration

**Build errors:**
- Ensure JDK 8 is installed and in PATH
- Verify Starsector installation path in build script
- Check that starfarer.api.jar and starfarer_obf.jar exist

## Known Issues

- This mod uses advanced hooking techniques and may need updates for future Starsector versions
- The rules.csv integration may require additional work to fully integrate with vanilla dialog flow
- Actual implementation may need reflection or more sophisticated dialog interception

## Version History

- **1.0.0**: Initial release

## License

Free to use and modify.
