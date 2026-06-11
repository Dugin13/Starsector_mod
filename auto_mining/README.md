# Auto Mining Industry Mod

A Starsector mod that adds a custom Mining industry called "Auto Mining" which demands **Heavy Armaments** and **Heavy Machinery** instead of Recreational Drugs.

## Features

- **New Industry**: Auto Mining
- **Changed Demand**: Heavy Armaments and Heavy Machinery (instead of vanilla Mining's Recreational Drugs)
- **Supplies**: Ore, Rare Ore, Organics (same as vanilla Mining)
- **Cost**: 150,000 credits to install, 1,000 credits/day upkeep
- **Build Time**: 180 days

## Installation

1. Copy the `auto_mining` folder to your Starsector mods directory:
   - Usually located at `Starsector/mods/`
2. Enable the mod in the Starsector launcher
3. Start a new game or load an existing save

## Usage

When establishing colonies, you'll see "Auto Mining" as an available industry option. It functions like the vanilla Mining industry but with the modified demand.

## Customization

To modify the industry further, edit `data/campaign/industries.csv`:
- **cost to install**: Initial construction cost
- **cost per day**: Daily maintenance cost
- **build time**: Days to construct
- **demand id 1-4**: Commodities the industry demands
- **supply id 1-4**: Commodities the industry produces

## Version History

- **1.0.0**: Initial release
