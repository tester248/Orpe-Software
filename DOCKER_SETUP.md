# Orpe Consultants - Docker Setup

## Quick Start

Start all services with one command:

```bash
docker-compose up --build
```

Once the app finishes starting (watch for "Tomcat started on port 8083"), access the application at:

**http://localhost:8083/login**

## Stopping the Application

```bash
docker-compose down
```

To also remove the database volume (clears all data):

```bash
docker-compose down -v
```

## Services

### MySQL Database (`orpe-mysql`)
- **Port**: 3306
- **Database**: db_orpe_consultants
- **User**: root
- **Password**: password
- **Persistent**: Data is stored in `mysql_data` volume

### Spring Boot Application (`orpe-app`)
- **Port**: 8083
- **URL**: http://localhost:8083
- **Auto-builds** from source code
- **Waits for MySQL** to be ready before starting

## Features

✅ One-command startup  
✅ Automatic database initialization from SQL dumps  
✅ Health checks ensure services start in correct order  
✅ Persistent MySQL data (survives restarts)  
✅ Auto-rebuild on code changes  

## Logs

View all logs:
```bash
docker-compose logs -f
```

View specific service logs:
```bash
docker-compose logs -f app      # Spring Boot app
docker-compose logs -f mysql    # MySQL database
```

## Troubleshooting

**App won't start?** Check the logs:
```bash
docker-compose logs app 2>&1 | tail -100
```

**Port already in use?** Change the port mapping in `docker-compose.yml`:
```yaml
ports:
  - "8084:8083"  # Now accessible at localhost:8084
```

**Database issues?** Reset everything:
```bash
docker-compose down -v
docker-compose up --build
```
