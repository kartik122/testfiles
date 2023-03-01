"""
Checks if a system of forces is in static equilibrium.
"""
from __future__ import annotations

from numpy import array, cos, cross, float64, radians, sin
from numpy.typing import NDArray



def in_static_equilibrium(
    forces: NDArray[float64], location: NDArray[float64], eps: float = 10**-1
) -> bool:
    moments: NDArray[float64] = cross(location, forces)
    sum_moments: float = sum(moments)
    return abs(sum_moments) < eps


if __name__ == "__main__":
    # Test to check if it works
    forces = array(
        [
            polar_force(718.4, 180 - 30),
            polar_force(879.54, 45),
            polar_force(100, -90),
        ]
    )

    location: NDArray[float64] = array([[0, 0], [0, 0], [0, 0]])

    assert in_static_equilibrium(forces, location)

    # Problem 1 in image_data/2D_problems.jpg
    forces = array(
        [
            polar_force(30 * 9.81, 15),
            polar_force(215, 180 - 45),
            polar_force(264, 90 - 30),
        ]
    )

    location = array([[0, 0], [0, 0], [0, 0]])

    assert in_static_equilibrium(forces, location)

    
    doctest.testmod()
