import java.util.*;

public class run {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String str = scanner.next();
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            ans.append('N');
        }

        int n = scanner.nextInt();
        int a = scanner.nextInt();
        int t = scanner.nextInt();

        Set<Integer> acceptingStates = new HashSet<>();
        for (int i = 0; i < a; i++) {
            int idx = scanner.nextInt();
            acceptingStates.add(idx);
        }

        Map<Pair, List<Integer>> transitions = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int num = scanner.nextInt();
            for (int j = 0; j < num; j++) {
                int ch = scanner.next().charAt(0);
                int newState = scanner.nextInt();

                Pair pair = new Pair(i, ch);
                if (!transitions.containsKey(pair)) {
                    List<Integer> nodes = new ArrayList<>();
                    nodes.add(newState);
                    transitions.put(pair, nodes);
                } else {
                    transitions.get(pair).add(newState);
                }
            }
        }

        Set<Integer> currentStates = new HashSet<>();
        currentStates.add(0);
        int currIndex = 0;

        while (currIndex < str.length()) {
            Set<Integer> newState = new HashSet<>();
            for (int node : currentStates) {
                Pair pair = new Pair(node, str.charAt(currIndex));
                if (!transitions.containsKey(pair)) continue;

                List<Integer> nodes = transitions.get(pair);
                for (int i = 0; i < nodes.size(); i++) {
                    if (acceptingStates.contains(nodes.get(i))) {
                        ans.setCharAt(currIndex, 'Y');
                    }
                    newState.add(nodes.get(i));
                }
            }
            currentStates = newState;
            currIndex++;
        }

        System.out.println(ans);
    }

    static class Pair {
        int first;
        int second;

        Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            Pair other = (Pair) obj;
            return first == other.first && second == other.second;
        }
    }
}
