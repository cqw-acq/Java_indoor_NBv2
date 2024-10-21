import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
public class zhishu_api {

    public static int[] zhishu(int n) {
        if (n < 2) {
            return new int[0];
        }

        boolean[] isPrime = new boolean[n + 1];
        Arrays.fill(isPrime, true);
        isPrime[0] = isPrime[1] = false;

        for (int i = 2; i * i <= n; i++) {
            if (isPrime[i]) {
                for (int j = i * i; j <= n; j += i) {
                    isPrime[j] = false;
                }
            }
        }

        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= n; i++) {
            if (isPrime[i]) {
                primes.add(i);
            }
        }

        return primes.stream().mapToInt(Integer::intValue).toArray();
    }

    static class zhishuHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            String response = "请提供整型";
            if (query != null && query.startsWith("n=")) {
                try {
                    int n = Integer.parseInt(query.substring(2));
                    int[] primes = zhishu(n);
                    response = Arrays.toString(primes);
                } catch (NumberFormatException e) {
                    response = "无效的整型输入";
                }
            }
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(5050), 0);
        server.createContext("/zhishu", new zhishu_api.zhishuHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("已开启");
    }

}