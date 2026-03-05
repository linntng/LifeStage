# Test guide
This guide describes practices used for writing frontend tests. The guide is written as an exercise to learn about testing, and will be updated continously.


## Running  tests

To execute unit tests with the [Vitest](https://vitest.dev/) test runner, use the following command:

```bash
ng test
```

For end-to-end (e2e) testing, run:

```bash
ng e2e
```
*end-to-end tests are not implemented yet

## Writing tests with Vitest


### Spying
A spy lets you observe how a function is being used without changing its actual behavior. This is useful when you want to verify not just the result of a function, but also *how* it was called.

```typescript
it("shows how Vitest spies work", () => {
    const spy = vi.spyOn(calculator, "add");
    const result = calculator.add(2, 3);
    expect(result).toBe(5);
    expect(spy).toHaveBeenCalledOnce();
    expect(spy).toHaveBeenCalledWith(2, 3);
})
```

### Mocking

To mock dependencies in your tests, you can use the `vi.mock()` function provided by Vitest. This allows you to replace real implementations with mock versions, making it easier to isolate and test specific components or services.

```typescript
it("shows how Vitest mocking works", () => {
    const spy = vi.spyOn(calculator, "add")
      .mockReturnValue(5);
    
    const result = calculator.add(2, 3);
    expect(result).toBe(5);
    expect(spy).toHaveBeenCalledOnce();
    expect(spy).toHaveBeenCalledWith(2, 3);
    
    // the result is always 5, due to mocking
    const result2 = calculator.add(5, 5);
    expect(result2).toBe(5);
})
```


