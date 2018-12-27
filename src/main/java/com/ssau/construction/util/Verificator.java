package com.ssau.construction.util;

import com.ssau.construction.domain.GasStation;

import java.util.ArrayList;

public class Verificator {

    public static ArrayList<VerificatorError> checkAll(GasStation gasStation) {
        ArrayList<VerificatorError> errors = new ArrayList<>();
        if (!isGasStationEntryDeparturePlacedCorrect(gasStation)) {
            errors.add(VerificatorError.IncorrectGasStationEntryDeparturePlacement);
        }
        if (!isServiceAreaEntryDeparturePlacedCorrect(gasStation)) {
            errors.add(VerificatorError.IncorrectServiceAreaEntryDeparturePlacement);
        }
        if (!checkGasStationPaths(gasStation)) {
            errors.add(VerificatorError.UnrelatedGasStationGraph);
        }
        if (!checkServiceAreaPaths(gasStation)) {
            errors.add(VerificatorError.UnrelatedServiceAreaGraph);
        }
        if (!hasCashBox(gasStation)) {
            errors.add(VerificatorError.hasNoCashBox);
        }
        if (!hasInfoTable(gasStation)) {
            errors.add(VerificatorError.hasNoInfoTable);
        }
        return errors;
    }

    private static boolean hasCashBox(GasStation gasStation) {
        return (gasStation.getCashBoxI() != -1);
    }

    private static boolean hasInfoTable(GasStation gasStation) {
        return (gasStation.getInfoTableI() != -1);
    }

    private static boolean isGasStationEntryDeparturePlacedCorrect(GasStation gasStation) {
        return (gasStation.getGasStationEntryI() < gasStation.getGasStationDepartureI())
                && (gasStation.getGasStationEntryJ() == gasStation.getFunctionalBlockV() - 1)
                && (gasStation.getGasStationDepartureJ() == gasStation.getFunctionalBlockV() - 1);
    }

    private static boolean isServiceAreaEntryDeparturePlacedCorrect(GasStation gasStation) {
        return (gasStation.getServiceAreaEntryI() < gasStation.getServiceAreaDepartureI())
                && (gasStation.getServiceAreaEntryJ() == gasStation.getServiceBlockV() - 1)
                && (gasStation.getServiceAreaDepartureJ() == gasStation.getServiceBlockV() - 1);
    }

    private static boolean checkGasStationPaths(GasStation gasStation) {
        GasStationGraph gasStationGraph = new GasStationGraph(gasStation);
        if (gasStationGraph.getEntry() != null) {
            for (Node node : gasStationGraph) {
                if (!gasStationGraph.isReachable(gasStationGraph.getEntry(), node)) {
                    return false;
                }
            }
            return true;
        } else return false;
    }

    private static boolean checkServiceAreaPaths(GasStation gasStation) {
        ServiceAreaGraph serviceAreaGraph = new ServiceAreaGraph(gasStation);
        if (serviceAreaGraph.getEntry() != null) {
            for (Node node : serviceAreaGraph) {
                if (!serviceAreaGraph.isReachable(serviceAreaGraph.getEntry(), node)) {
                    return false;
                }
            }
            return true;
        } else return false;
    }
}
