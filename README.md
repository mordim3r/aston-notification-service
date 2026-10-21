 docker run -d -p 1025:1025 -p 8025:8025 mailhog/mailhog
postman->post-> http://localhost:8081/api/notifications/send->
{
  "email": "test@example.com",
  "text": "111111111"
}
