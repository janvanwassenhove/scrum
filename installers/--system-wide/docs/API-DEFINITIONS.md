# API Definitions

SCRUM supports first-class HTTP API definitions using declarative syntax that aligns with the narrative style of the language.

## Defining an API

Define an API with a base path:

```SCRUM
I WANT TO DEFINE API "InvoiceApi"
    BASE IS "/api/invoices"
END OF API
```

## Defining Endpoints

Endpoints can be nested within API blocks and support various HTTP methods and properties:

```SCRUM
I WANT TO DEFINE API "InvoiceApi"
    BASE IS "/api/invoices"

    I WANT TO DEFINE ENDPOINT "GetCustomerInvoices"
        METHOD IS "GET"
        PATH IS "/customer/{customerId}"
        QUERY_PARAMS ARE { "status", "year" }
        RETURNS IS "Invoice[]"
    END OF ENDPOINT

    I WANT TO DEFINE ENDPOINT "GetInvoiceById"
        METHOD IS "GET"
        PATH IS "/{invoiceId}"
        RETURNS IS "Invoice"
    END OF ENDPOINT

    I WANT TO DEFINE ENDPOINT "CreateInvoice"
        METHOD IS "POST"
        PATH IS "/create"
        RETURNS IS "Invoice"
    END OF ENDPOINT

END OF API
```

## Executable Endpoints with WHEN REQUEST

Endpoints can include executable handler logic using `WHEN REQUEST` blocks:

```SCRUM
I WANT TO DEFINE API "Greeting API"
    BASE IS "/api/v1/greetings"

    I WANT TO DEFINE ENDPOINT "Greet User"
        METHOD IS "GET"
        PATH IS "/hello/{name}"
        RETURNS IS "String"
        
        WHEN REQUEST
            SAY "┌─────────────────────────┐"
            SAY "│   Greeting Endpoint     │"
            SAY "├─────────────────────────┤"
            SAY "│ Endpoint was called!    │"
            SAY "└─────────────────────────┘"
        END WHEN
    END OF ENDPOINT

END OF API
```

The `WHEN REQUEST` block can contain any valid SCRUM statements, including:
- Variable assignments
- Function calls
- Conditional logic (IF/ELSE)
- Loops (I WANT TO ITERATE)
- Output statements (SAY)

## API Keywords

| Keyword         | Purpose                                          | Example                          |
|-----------------|--------------------------------------------------|----------------------------------|
| `I WANT TO DEFINE` | Introduces an API or endpoint definition      | `I WANT TO DEFINE API "UserApi"` |
| `API`           | Declares an API block                            | `I WANT TO DEFINE API "UserApi"` |
| `ENDPOINT`      | Declares an endpoint within an API               | `I WANT TO DEFINE ENDPOINT "GetUsers"` |
| `BASE`          | Specifies the base path for an API               | `BASE IS "/api/users"`           |
| `METHOD`        | Specifies the HTTP method for an endpoint        | `METHOD IS "GET"`                |
| `PATH`          | Specifies the URL path for an endpoint           | `PATH IS "/users/{id}"`          |
| `QUERY_PARAMS`  | Declares query parameters for an endpoint        | `QUERY_PARAMS ARE { "page", "limit" }` |
| `RETURNS`       | Specifies the return type of an endpoint         | `RETURNS IS "User[]"`            |
| `WHEN`          | Marks the beginning of a request handler block   | `WHEN REQUEST`                   |
| `REQUEST`       | Follows WHEN to indicate request handling        | `WHEN REQUEST`                   |
| `END WHEN`      | Closes a WHEN REQUEST block                      | `END WHEN`                       |
| `END OF API`    | Closes an API definition block                   | `END OF API`                     |
| `END OF ENDPOINT` | Closes an endpoint definition block            | `END OF ENDPOINT`                |

## Complete API Examples

See these examples for complete working demonstrations:
- [development/examples/ApiExample.scrum](../development/examples/ApiExample.scrum) - Multiple API definitions with various HTTP methods
- [development/examples/ExecutableApiExample.scrum](../development/examples/ExecutableApiExample.scrum) - Executable endpoints with WHEN REQUEST blocks
- [development/examples/AgeCalculatorApi.scrum](../development/examples/AgeCalculatorApi.scrum) - Interactive API with user input
- [development/examples/SimpleAgeApi.scrum](../development/examples/SimpleAgeApi.scrum) - Simple age calculator with formatted output
