# GriefPrevention — Forged with Alloy

Land claim and anti-griefing mod for Minecraft, rebuilt from scratch for the [Alloy](https://github.com/arch-linux/alloy) modding ecosystem.

> Based on the original [GriefPrevention](https://github.com/GriefPrevention/GriefPrevention) by BigScary. Rebuilt entirely — no Bukkit, no Spigot, no legacy code.

## Features

- **Land claiming** — Players create rectangular claims with a golden shovel
- **Trust system** — Hierarchical permissions: Access, Container, Build, Manage, Edit
- **PvP protection** — Safe zones inside claims, combat logging prevention
- **Grief prevention** — Block break/place, entity damage, explosion, fire spread protection
- **Admin tools** — Admin claims, claim investigation, size restrictions, cleanup
- **Automatic claim blocks** — Players earn claim blocks over time
- **Flat file storage** — JSON-based, one file per claim and player

## Building

Requires the [Alloy](https://github.com/arch-linux/alloy) repo cloned alongside this one (default: `~/Desktop/alloy`).

```bash
# Build the alloy-api and alloy-loader JARs first
cd ~/Desktop/alloy
./gradlew :alloy-api:jar :alloy-loader:jar

# Build GriefPrevention
cd ~/Desktop/GriefPrevention
./gradlew jar
```

The JAR is output to `build/libs/GriefPrevention-<version>-mc<mcVersion>.jar`.

### Release to Desktop

```bash
./gradlew release
```

Builds the JAR and copies it to `~/Desktop/`.

### Custom Alloy location

```bash
./gradlew jar -PalloyHome=/path/to/alloy
```

## 30+ Commands

| Command | Description |
|---|---|
| `/claim` | Create a claim at your location |
| `/unclaim` `/abandonclaim` | Remove your claim |
| `/trust <player>` | Grant build trust |
| `/containertrust <player>` | Grant container access |
| `/accesstrust <player>` | Grant interaction access |
| `/managetrust <player>` | Grant management rights |
| `/untrust <player>` | Revoke trust |
| `/trustlist` | View trust in current claim |
| `/claimlist` | List your claims |
| `/adminclaims` | Toggle admin claim mode |
| `/claiminfo` | View claim details |
| ...and more | Full command list in game with `/gp help` |

## License

[MIT](LICENSE.txt)
