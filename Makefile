db-up:
	docker-compose up -d --force-recreate

# volume 삭제
db-down:
	docker-compose down -v