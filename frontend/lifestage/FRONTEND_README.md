# Frontend guide for Lifestage

This project was generated using [Angular CLI](https://github.com/angular/angular-cli) version 21.1.4.

## Development server

To start a local development server, run:

```bash
ng serve
```

Once the server is running, open your browser and navigate to `http://localhost:4200/`. The application will automatically reload whenever you modify any of the source files.

## For developers

### Code formatting and linting

This project uses [Eslint](https://eslint.org/) for linting and [Prettier](https://prettier.io/) for code formatting.

- For linting, run: `npm run lint`
- For code formatting, run: `npm run format`

> Remember to run linting and formatting before committing your code!

**Configure Prettier in VS Code for automatic formatting on save:**

1. Install the VSCode Prettier extension.
2. Open settings (Ctrl + ,) and search for "Format On Save".
3. Enable “Editor: Format On Save”.
4. Set Prettier as the default formatter: Find "Default formatter" in settings and set it to _Prettier_

### Code scaffolding

Angular CLI includes powerful code scaffolding tools. To generate a new component, run:

```bash
ng generate component component-name
```

For a complete list of available schematics (such as `components`, `directives`, or `pipes`), run:

```bash
ng generate --help
```

### Building

To build the project run:

```bash
ng build
```

This will compile your project and store the build artifacts in the `dist/` directory. By default, the production build optimizes your application for performance and speed.

### Running unit tests

To execute unit tests with the [Vitest](https://vitest.dev/) test runner, use the following command:

```bash
ng test
```

### Running end-to-end tests (not implemented yet)

For end-to-end (e2e) testing, run:

```bash
ng e2e
```