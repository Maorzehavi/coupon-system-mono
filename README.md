# Coupons system

This is a coupon system application that allows companies to create and distribute coupons to customers. The application
is built using the following technologies:

- Java 17
- Spring Boot 3.0.2
- Spring Data JPA
- Spring Web
- Spring Mail
- Spring Security
- JWT
- MySQL
- Hibernate - validator
- Lombok

## Security
the filter chain is as follows:
- DisableEncodeUrlFilter
- WebAsyncManagerIntegrationFilter
- SecurityContextHolderFilter
- HeaderWriterFilter
- CorsFilter
- LogoutFilter
- JwtAuthenticationFilter
- RequestCacheAwareFilter
- SecurityContextHolderAwareRequestFilter
- AnonymousAuthenticationFilter
- SessionManagementFilter
- ExceptionTranslationFilter
- AuthorizationFilter

## How to run the application

- Clone the repository
- Create a database in MySQL
- Update the application.yml file with your database credentials
- Run the application
- The application will be available at http://localhost:8080

## How to use the application

- The application has three roles: Admin, Company and Customer
- Admin can create manage companies and customers
- Company can create coupons and manage them
- Customer can add coupons to his cart and purchase them

## Application endpoints

### register

- POST http://localhost:8080/api/v1/auth/register-company
  body example:
  
{
"data":{
"name":"company name",
"phoneNumber":"+9874844143"
},
"user":{
"email":"email@email.com",
"password":"password"
}
}

- POST http://localhost:8080/api/v1/auth/register-customer
  body example:

  {
  "data":{
  "firstName":"first",
  "lastName":"last"
  },
  "user":{
  "email":"email@email.com",
  "password":"password"
  }
  }

### login

- POST http://localhost:8080/api/v1/auth/login
  body example:
  {
  "email":"email@email.com",
  "password":"password"
  }


## Support

For support, email maorzehavi@outlook.com 
there is a video of the application in action in the zip file

## License

[MIT](https://choosealicense.com/licenses/mit/)








