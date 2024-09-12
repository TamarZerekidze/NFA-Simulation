import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Scanner;



public class build {
    public static class NFA {

        private Set<Integer> acceptingStates;
        private int N;
        private Map<Integer, List<Action>> transitions;

        public NFA(String string) {
            acceptingStates = new HashSet<>();
            acceptingStates.add(string.length());
            transitions = new HashMap<>();
            N = string.length() + 1;

            for (int i = 0; i < string.length(); i++) {
                addTransitions(string.charAt(i), i, i + 1);
            }
        }

        private void addTransitions(char character, int fromState, int toState) {
            if (transitions.containsKey(fromState)) {
                List<Action> actionList = transitions.get(fromState);
                boolean exists = false;
                for (Action action : actionList) {
                    if (action.character == character && action.toState == toState) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    actionList.add(new Action(character, toState));
                }
            } else {
                List<Action> actionList = new ArrayList<>();
                actionList.add(new Action(character, toState));
                transitions.put(fromState, actionList);
            }
        }

        public void concatenation(NFA nfa2) {
            int N2 = nfa2.getN();
            Set<Integer> otherAcceptingStates = nfa2.getAccepting();
            Map<Integer, List<Action>> otherTransitions = nfa2.getActions();

            for (int fromState : otherTransitions.keySet()) {
                List<Action> actionList = otherTransitions.get(fromState);
                for (Action action : actionList) {
                    if (fromState == 0) {
                        for (int acc : acceptingStates) {
                            addTransitions(action.character, acc, action.toState + N - 1);
                        }
                    } else {
                        addTransitions(action.character, fromState + N - 1, action.toState + N - 1);
                    }
                }
            }

            if (!otherAcceptingStates.contains(0)) {
                acceptingStates = new HashSet<>();
            }

            for (int acc : otherAcceptingStates) {
                int newAcc = acc + N - 1;
                acceptingStates.add(newAcc);
            }

            N += N2 - 1;
        }

        public void star() {
            if (N == 1) {
                return;
            }

            for (int acc : acceptingStates) {
                List<Action> actionList = transitions.get(0);
                for (Action action : actionList) {
                    addTransitions(action.character, acc, action.toState);
                }
            }

            acceptingStates.add(0);
        }

        public void union(NFA nfa2) {
            int N2 = nfa2.getN();
            Set<Integer> otherAcceptingStates = nfa2.getAccepting();
            Map<Integer, List<Action>> otherTransitions = nfa2.getActions();

            for (int fromState : otherTransitions.keySet()) {
                List<Action> actionList = otherTransitions.get(fromState);
                for (Action action : actionList) {
                    int newFrom = (fromState > 0) ? fromState + N - 1 : 0;
                    addTransitions(action.character, newFrom, action.toState + N - 1);
                }
            }

            for (int acc : otherAcceptingStates) {
                int newAcc = (acc > 0) ? acc + N - 1 : 0;
                acceptingStates.add(newAcc);
            }

            N += N2 - 1;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            int a = acceptingStates.size();
            int t = transitions.values().stream().mapToInt(List::size).sum();

            sb.append(N).append(" ").append(a).append(" ").append(t).append("\n");
            List<Integer> sortedAccepting = new ArrayList<>(acceptingStates);
            sortedAccepting.sort(Integer::compareTo);
            int sz = sb.length();
            sb.append(sortedAccepting.stream().map(Object::toString).reduce("", (acc, val) -> acc + " " + val));
            sb.deleteCharAt(sz);
            for (int key = 0; key < N; key++) {
                if (!transitions.containsKey(key)) {
                    sb.append("\n0");
                    continue;
                }

                List<Action> actionList = transitions.get(key);
                sb.append("\n").append(actionList.size());
                for (Action action : actionList) {
                    sb.append(" ").append(action.character).append(" ").append(action.toState);
                }
                sb.append(" ");
            }

            return sb.toString();
        }

        public int getN() {
            return N;
        }

        public Set<Integer> getAccepting() {
            return acceptingStates;
        }

        public Map<Integer, List<Action>> getActions() {
            return transitions;
        }

        private static class Action {
            private char character;
            private int toState;

            public Action(char character, int toState) {
                this.character = character;
                this.toState = toState;
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String regex = scanner.nextLine();
        NFA nfa = regexToNFA(regex);
        System.out.println(nfa);
    }

    private static NFA regexToNFA(String regex) {
        int i = regex.length() - 1;
        if (i == 0) {
            return new NFA(regex);
        }

        int equality = 0;
        for (int idx = 0; idx < regex.length(); idx++) {
            if (regex.charAt(idx) == '(') {
                equality++;
            } else if (regex.charAt(idx) == ')') {
                equality--;
            } else if (regex.charAt(idx) == '|' && equality == 0) {
                NFA left = regexToNFA(regex.substring(0, idx));
                NFA right = regexToNFA(regex.substring(idx + 1));
                left.union(right);
                return left;
            }
        }

        String curStr = "";
        while (i >= 0) {
            if (regex.charAt(i) == '*') {
                if (regex.charAt(i - 1) == ')') {
                    int j = i - 2;
                    int count = 1;
                    while (j >= 0) {
                        if (regex.charAt(j) == ')') {
                            count++;
                        }
                        if (regex.charAt(j) == '(') {
                            count--;
                        }
                        if (count == 0) {
                            break;
                        }
                        j--;
                    }
                    NFA mid = regexToNFA(regex.substring(j + 1, i - 1));
                    NFA right = regexToNFA(curStr);
                    mid.star();
                    mid.concatenation(right);
                    if (j > 0) {
                        NFA left = regexToNFA(regex.substring(0, j - 1));
                        left.concatenation(mid);
                        mid = left;
                    }
                    return mid;
                } else {
                    NFA right = new NFA(regex.substring(i - 1, i));
                    right.star();
                    if (i <= 1) return right;
                    NFA left = regexToNFA(regex.substring(0, i - 1));
                    left.concatenation(right);
                    return left;

                }
            } else if (regex.charAt(i) == ')') {
                int j = i - 1;
                int count = 1;
                while (j >= 0) {
                    if (regex.charAt(j) == ')') {
                        count++;
                    }
                    if (regex.charAt(j) == '(') {
                        count--;
                    }
                    if (count == 0) {
                        break;
                    }
                    j--;
                }
                NFA mid = regexToNFA(regex.substring(j + 1, i));
                NFA right = regexToNFA(regex.substring(i + 1));
                mid.concatenation(right);
                if (j > 0) {
                    NFA left = regexToNFA(regex.substring(0, j));
                    left.concatenation(mid);
                    mid = left;
                }
                return mid;
            } else {
                curStr = regex.charAt(i) + curStr;
            }

            i--;
        }

        return new NFA(curStr);
    }
}