package DavexCenter.module.task.controller;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import DavexAgent.module.task.service.SecretFlowService;
import DavexBase.common.My;

import java.io.BufferedReader;
import java.io.InputStreamReader;


@RestController
@RequestMapping("/SecretFlowTask")
public class SecretFlowController {


    @Autowired
    private SecretFlowService secretFlowService;

    @Autowired
    private My my;
    @GetMapping("/execute-script")
    public String executeScript() {
        try {
            // 定义要执行的命令
            String command = "source sfenv/bin/activate && python testFL.py";
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("bash", "-c", command);
            processBuilder.directory(new java.io.File("/home/zw/SFFL"));

            // 启动进程并获取输出
            Process process = processBuilder.start();

            // 捕获标准输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 捕获错误输出
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder errorOutput = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return "Script executed successfully: \n" + output.toString();
            } else {
                return "Script execution failed with exit code: " + exitCode + "\nError Output: " + errorOutput.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }
    }

    @GetMapping("/activate-mainRay")
    public String activateMainRay(
            @RequestParam String ip,
            @RequestParam String port,
            @RequestParam String name
            // 参数化端口
    ) {
        // 构造命令字符串
        String command = String.format("source sfenv/bin/activate && ray start --head --node-ip-address=\"%s\" --port=\"%s\" --resources='{\"%s\": 16}' --include-dashboard=False --disable-usage-stats", ip, port,name);

        // 调用 service 中的方法执行命令
        return secretFlowService.executeCommand(command);
    }
    @GetMapping("/stop-ray")
    public String stopMainRay() {
        // 调用 service 中的方法执行命令
        return secretFlowService.executeCommand("source sfenv/bin/activate && ray stop");
    }
}
