# Vouched Java Example

This example repo consists of two parts

1. Tests to highlight usage of [REST API](https://docs.vouched.id/#section/Submit-a-verification/REST-Submit-job)
2. Development Server to highlight [JS Plugin](https://docs.vouched.id/#section/Submit-a-verification/JS-Plugin)

## REST API

#### 1. Install dependencies

```shell
# install mvn if not present on your machine
# https://www.baeldung.com/install-maven-on-windows-linux-mac#:~:text=We%20run%20command%20sudo%20apt,install%20the%20latest%20Apache%20Maven.&text=This%20will%20take%20a%20few,version%20to%20verify%20our%20installation.
mvn clean install
```

#### 2. Add your Private Key

- If necessary, [create a Private Key](https://docs.vouched.id/#section/Dashboard/Manage-keys)
- Open `src/main/resources/config.properties`
- Replace `<API_KEY>` with your Private Key
#### 3. Run the tests

```shell
# run all tests
mvn test

# run a specific test
mvn -Dtest=AppTest#UnauthorizedInvites test
#AppTest is the name of the class
#UnauthorizedInvites is the name of the test method to execute
```

#### 4. View each endpoint and learn more

- [Jobs Endpoints](https://docs.vouched.id/#tag/jobs)
- [Invites Endpoints](https://docs.vouched.id/#tag/invites)
- [Aamva Enpoints](https://docs.vouched.id/#tag/aamva)
- [Crosscheck](https://docs.vouched.id/#tag/crosscheck)

## JSPlugin

### 1. Install dependencies

```shell
mvn clean install
```

### 2. Add your Public Key

- If necessary, [create a Public Key](https://docs.vouched.id/#section/Dashboard/Manage-keys)
- Open `src/main/resources/static/configureVouched.js`
- Replace `<PUBLIC_KEY>` with your Public Key

### 3. Start Development Server

```shell
 mvn spring-boot:run
```

In a browser, head to http://localhost:8085 to see the Vouched JS Plugin in action.

### 4. Modify configuration for the Plugin

Feel free to modify any configurations in `static/configureVouched.js`. Here is the full list of [configuration options](https://docs.vouched.id/#section/SDKs/JS-Plugin)
