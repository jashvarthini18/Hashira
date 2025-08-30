import java.io.*;
import java.math.BigInteger;
import java.nio.file.*;
import java.util.*;

class Share {
    String id;
    BigInteger x;
    BigInteger y;

    Share(String id, BigInteger x, BigInteger y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
}

class ParsedInput {
    int n, k;
    List<Share> shares;

    ParsedInput(int n, int k, List<Share> shares) {
        this.n = n;
        this.k = k;
        this.shares = shares;
    }
}

public class Test {


public static ParsedInput parseInput(String input) {
    input = input.replaceAll("[\\n\\r]", "").trim();

    int n = -1, k = -1;

    java.util.regex.Pattern pn = java.util.regex.Pattern.compile("\"n\"\\s*:\\s*(\\d+)");
    java.util.regex.Matcher mn = pn.matcher(input);
    if (mn.find()) {
        n = Integer.parseInt(mn.group(1));
    }

    java.util.regex.Pattern pk = java.util.regex.Pattern.compile("\"k\"\\s*:\\s*(\\d+)");
    java.util.regex.Matcher mk = pk.matcher(input);
    if (mk.find()) {
        k = Integer.parseInt(mk.group(1));
    }

    List<Share> shares = new ArrayList<>();

    java.util.regex.Pattern p = java.util.regex.Pattern.compile(
        "\"(\\d+)\"\\s*:\\s*\\{\\s*\"base\"\\s*:\\s*\"(\\d+)\"\\s*,\\s*\"value\"\\s*:\\s*\"([0-9A-Fa-f]+)\"\\s*}"
    );
    java.util.regex.Matcher m = p.matcher(input);

    while (m.find()) {
        String id = m.group(1);
        int base = Integer.parseInt(m.group(2));
        String valueStr = m.group(3);
        BigInteger value = new BigInteger(valueStr, base);
        BigInteger x = new BigInteger(id);
        shares.add(new Share(id, x, value));
    }

    return new ParsedInput(n, k, shares);
}


    public static BigInteger lagrangeConstantTerm(List<Share> subset) {
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < subset.size(); i++) {
            Share si = subset.get(i);

            BigInteger num = BigInteger.ONE;
            BigInteger den = BigInteger.ONE;

            for (int j = 0; j < subset.size(); j++) {
                if (i == j) continue;
                Share sj = subset.get(j);

                num = num.multiply(sj.x.negate());    
                den = den.multiply(si.x.subtract(sj.x));
            }
            BigInteger li = num.divide(den);
            result = result.add(si.y.multiply(li));
        }

        return result;
    }

    public static BigInteger evaluatePolynomialAtX(List<Share> subset, BigInteger x) {
        BigInteger recomputed = BigInteger.ZERO;

        for (int i = 0; i < subset.size(); i++) {
            Share si = subset.get(i);

            BigInteger num = BigInteger.ONE;
            BigInteger den = BigInteger.ONE;
            for (int j = 0; j < subset.size(); j++) {
                if (i == j) continue;
                Share sj = subset.get(j);

                num = num.multiply(x.subtract(sj.x));
                den = den.multiply(si.x.subtract(sj.x));
            }
            BigInteger li = num.divide(den);
            recomputed = recomputed.add(si.y.multiply(li));
        }

        return recomputed;
    }

    public static void main(String[] args) throws Exception {
        String input;
        if (args.length > 0) {
            input = new String(Files.readAllBytes(Paths.get(args[0])));
        } else {
            input = new String(System.in.readAllBytes());
        }

        ParsedInput data = parseInput(input);

        BigInteger secret = null;
        List<String> validShares = new ArrayList<>();
        List<String> corruptedShares = new ArrayList<>();
        List<Share> correctSubset = null;

        List<List<Share>> subsets = generateSubsets(data.shares, data.k);

        for (List<Share> subset : subsets) {
            BigInteger candidateSecret = lagrangeConstantTerm(subset);

            int validCount = 0;
            for (Share s : data.shares) {
                BigInteger recomputed = evaluatePolynomialAtX(subset, s.x);
                if (recomputed.equals(s.y)) {
                    validCount++;
                }
            }

            if (validCount >= data.k) {
                secret = candidateSecret;
                correctSubset = subset;
                break;
            }
        }

        if (correctSubset != null) {
            for (Share s : data.shares) {
                BigInteger recomputed = evaluatePolynomialAtX(correctSubset, s.x);
                if (recomputed.equals(s.y)) {
                    validShares.add(s.id);
                } else {
                    corruptedShares.add(s.id);
                }
            }
        }

        System.out.println("Secret = " + secret);
        System.out.println("Valid shares: " + validShares);
        System.out.println("Corrupted shares: " + corruptedShares);
    }


    public static List<List<Share>> generateSubsets(List<Share> arr, int k) {
        List<List<Share>> result = new ArrayList<>();
        backtrack(arr, k, 0, new ArrayList<>(), result);
        return result;
    }

    public static void backtrack(List<Share> arr, int k, int start, List<Share> current, List<List<Share>> result) {
        if (current.size() == k) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = start; i < arr.size(); i++) {
            current.add(arr.get(i));
            backtrack(arr, k, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }
}