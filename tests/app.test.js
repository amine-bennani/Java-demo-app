const { add } = require('../app');

test('adds two numbers', () => {
  expect(add(2, 3)).toBe(5);
});

