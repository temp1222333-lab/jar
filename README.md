# CustomArmorTrims

A production-ready cosmetic armor trim system plugin for Paper/Folia 1.20+

## Features

### Core Features
- **Automatic Armor Trim Application** - Detect when players equip armor and apply selected trims
- **Armor Trim Customization GUI** - Interactive menu system for selecting trims
- **Pattern Selection** - 12 unique armor trim patterns with permission-based access
- **Material Selection** - 10 different trim materials with permission-based access
- **Combined Permission System** - Granular control over which patterns and materials each player can use
- **Database Support** - SQLite and MySQL
- **PlaceholderAPI Support** - Integrate trims data into other plugins
- **Live Preview** - See trims applied immediately after selection
- **Configuration** - Fully customizable messages, sounds, and GUI settings
- **Hot Reload** - Reload configuration without restarting the server
- **Folia Compatible** - Fully compatible with Folia scheduler

## Commands

- `/armortrims` or `/at` - Open the main customization GUI
- `/armortrims reload` - Reload plugin configuration
- `/armortrims reset` - Reset your armor trim selections

## Permissions

### Main Permissions
- `armortrims.use` - Allows using the armor trims system (default: true)
- `armortrims.admin` - Admin access to all features
- `armortrims.reload` - Reload plugin configuration (default: op)
- `armortrims.reset` - Reset your armor trims (default: true)

### Pattern Permissions
- `armortrims.pattern.*` - Access to all patterns
- `armortrims.pattern.sentry` through `armortrims.pattern.host` - Individual pattern access

### Material Permissions
- `armortrims.material.*` - Access to all materials
- `armortrims.material.iron` through `armortrims.material.copper` - Individual material access

## Database Configuration

### SQLite (Default)
```yaml
database:
  type: sqlite
  sqlite:
    file: plugins/CustomArmorTrims/data.db
```

### MySQL
```yaml
database:
  type: mysql
  mysql:
    host: localhost
    port: 3306
    database: customarmortrims
    username: root
    password: password
    max-pool-size: 10
```

## PlaceholderAPI Placeholders

- `%armortrim_pattern%` - Current selected pattern
- `%armortrim_material%` - Current selected material
- `%armortrim_full%` - Full trim information (pattern + material)

## Performance

- **Async Database Operations** - All database calls are non-blocking
- **Player Data Caching** - Configurable cache with TTL
- **Connection Pooling** - HikariCP for efficient database connections
- **Folia Scheduler Support** - Compatible with regional multithreading
- **Supports 200+ Concurrent Players** - Optimized for large servers

## Building

```bash
mvn clean package
```

The compiled JAR will be in the `target/` directory.

## Configuration Files

- `config.yml` - Main plugin configuration
- `messages.yml` - All customizable messages and strings

## Requirements

- Java 21+
- Paper 1.20.4+
- Folia 1.20.4+ (optional)
- PlaceholderAPI (optional)

## License

All rights reserved.
