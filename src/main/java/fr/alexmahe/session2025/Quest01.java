package fr.alexmahe.session2025;

import fr.alexmahe.common.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fr.alexmahe.common.Utils.RESOURCES;

@Slf4j
public class Quest01 {

    public static void main(String[] args) throws IOException {
        var today = new Quest01();
        var index = 0;

        // PART 01
        var input01 = Utils.readInputSplitOnEmptyLines(RESOURCES.formatted("2025", "quest01", "input01"));
        var names = input01.get(0).split(",");
        index = today.part01(input01, names, index);
        log.info("Nom final : {}", names[index]);

        // PART 02
        var input02 = Utils.readInputSplitOnEmptyLines(RESOURCES.formatted("2025", "quest01", "input02"));
        var parentNames = input02.get(0).split(",");
        index = 0;
        index = today.part02(input02, parentNames, index);
        log.info("Nom parent : {}", parentNames[index]);

        // PART 03
        var input03 = Utils.readInputSplitOnEmptyLines(RESOURCES.formatted("2025", "quest01", "input03"));
        var parentNamesSwap = new ArrayList<>(List.of(input03.get(0).split(",")));
        today.part03(input03, parentNamesSwap);
        log.info("Nom 2e parent : {}", parentNamesSwap.get(0));
    }

    private int part01(List<String> input, String[] names, int index) {
        var instructions = input.get(1).split(",");
        var length = names.length;
        for (String instruction : instructions) {
            var direction = Directions.getDirFromChar(instruction.charAt(0));
            var instructionMoveQuantity = Integer.parseInt(instruction.substring(1));
            int calculatedMoveQuantity;

            switch (direction) {
                case LEFT -> {
                    calculatedMoveQuantity = Math.min(index, instructionMoveQuantity);
                }
                case RIGHT -> {
                    calculatedMoveQuantity = Math.min(length - 1 - index, instructionMoveQuantity);
                }
                default -> {
                    throw new IllegalArgumentException("Invalid direction: " + direction);
                }
            }

            index =  index + (direction.increment * calculatedMoveQuantity);
        }
        return index;
    }

    private int part02(List<String> input, String[] names, int index) {
        var instructions = input.get(1).split(",");
        var length = names.length;
        for (String instruction : instructions) {
            var direction = Directions.getDirFromChar(instruction.charAt(0));
            var instructionMoveQuantity = Integer.parseInt(instruction.substring(1));
            index = (index + (direction.increment * instructionMoveQuantity)) % length;
            index = index < 0 ? length + index : index;
        }
        return index;
    }

    private void part03(List<String> input, ArrayList<String> parentNames) {
        var instructions = input.get(1).split(",");
        for  (String instruction : instructions) {
            var direction = Directions.getDirFromChar(instruction.charAt(0));
            var instructionMoveQuantity = Integer.parseInt(instruction.substring(1));
            var indexToSwap = direction.increment * instructionMoveQuantity % parentNames.size();
            indexToSwap = indexToSwap < 0 ? parentNames.size() + indexToSwap : indexToSwap;

            swapListElement(parentNames, 0, indexToSwap);
        }
    }

    private <T> void swapListElement(ArrayList<T> list, int index01, int index02) {
        var listElement = list.get(index01);
        list.set(index01, list.get(index02));
        list.set(index02, listElement);
    }

    @AllArgsConstructor
    private enum Directions {
        LEFT("L", -1),
        RIGHT("R", 1);

        private final String character;
        private final int increment;

        public static Directions getDirFromChar(char character) {
            return Arrays.stream(Directions.values())
                    .filter(direction -> direction.character.equals(String.valueOf(character)))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid character: " + character));
        }
    }
}
