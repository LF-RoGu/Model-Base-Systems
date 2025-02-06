# Model-Base-Systems

This repository contains all the files for the Model-Based Systems WiSe 24/25 project on [C-ITS](https://www.car-2-car.org/about-c-its).

## Organization
Team members and matriculation number:

    -   Hazhir Amiri, 7218509
    -   Luis Fernando Rodriguez Gutierrez, 7219085

## Structure
```

├── Blockweek/             : All exercises and models from the project, the block week, etc
    ├────Vehicle           : Amalthea model of the vehicle components
    ├────cits_obu          : Amalthea model of the OBU
    ├────rsu_model         : Amalthea model of the RSU
├── docs/                  : Some sources and references 
├── Presentation/          : Presentation file
├── README.md              : this file
```

## Model
The model is divided into three main parts: the vehicle, the OBU, and the RSU. Together, these three make up the HLN - SV scenario that has been modeled.

### Vehicle
Contains the vehicle components that gather data from different parts of the vehicle, e.g. ECU, TCM, etc. These data are aggregated by the VCM to provide DTC information which will be used by the OBU to determine the status of the vehicle.

### OBU
The C-ITS onboard unit, an addition to the vehicle, communicates with the C-ITS infrastructure to provide and receive information. In the modeled scenario, it sends and receives DENM messages relating to the HLN - SV scenario.

### RSU
The roadside unit is separate from the vehicle and is a standalone device that manages the communication between OBUs and the underlying infrastructure.
