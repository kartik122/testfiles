
from sympy import diff, lambdify, symbols
from sympy.functions import *  # noqa: F401, F403


def newton_raphson(
    function: str,
    starting_point: complex,
    variable: str = "x",
    precision: float = 10**-10,
    multiplicity: int = 1,
) -> complex:
    x = symbols(variable)
    func = lambdify(x, function)
    diff_function = lambdify(x, diff(function, x))

    prev_guess = starting_point

    while True:
        if diff_function(prev_guess) != 0:
            next_guess = prev_guess - multiplicity * func(prev_guess) / diff_function(
                prev_guess
            )
        else:
            raise ZeroDivisionError("Could not find root") from None

        # Precision is checked by comparing the difference of consecutive guesses
        if abs(next_guess - prev_guess) < precision:
            return next_guess

        prev_guess = next_guess


# Let's Execute
if __name__ == "__main__":
    # Find root of trigonometric function
    # Find value of pi
    print(f"The root of sin(x) = 0 is {newton_raphson('sin(x)', 2)}")
    print(f"The root of cos(x) = 0 is {newton_raphson('cos(x)', 0)}")
