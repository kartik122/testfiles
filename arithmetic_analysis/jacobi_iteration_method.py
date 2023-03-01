from __future__ import annotations

import numpy as np
from numpy import float64
from numpy.typing import NDArray


# Method to find solution of system of linear equations
def jacobi_iteration_method(
    coefficient_matrix: NDArray[float64],
    constant_matrix: NDArray[float64],
    init_val: list[int],
    iterations: int,
) -> list[float]:

    if rows1 != cols1:
        raise ValueError(
            f"Coefficient matrix dimensions must be nxn but received {rows1}x{cols1}"
        )

    if cols2 != 1:
        raise ValueError(f"Constant matrix must be nx1 but received {rows2}x{cols2}")

    if rows1 != rows2:
        raise ValueError(
            f"""Coefficient and constant matrices dimensions must be nxn and nx1 but
            received {rows1}x{cols1} and {rows2}x{cols2}"""
        )

    if len(init_val) != rows1:
        raise ValueError(
            f"""Number of initial values must be equal to number of rows in coefficient
            matrix but received {len(init_val)} and {rows1}"""
        )

    if iterations <= 0:
        raise ValueError("Iterations must be at least 1")

    table: NDArray[float64] = np.concatenate(
        (coefficient_matrix, constant_matrix), axis=1
    )

    rows, cols = table.shape

    strictly_diagonally_dominant(table)

    return [float(i) for i in new_val]


if __name__ == "__main__":
    import doctest

    doctest.testmod()
