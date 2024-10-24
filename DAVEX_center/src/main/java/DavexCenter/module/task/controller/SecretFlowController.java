package DavexCenter.module.task.controller;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;

import DavexCenter.module.task.service.SecretFlowService;
import DavexCenter.module.file.controller.FlFileController;
import DavexCenter.module.file.service.FileService;

import DavexBase.common.Body;
import DavexBase.common.My;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/SecretFlowTask")
public class SecretFlowController {
    @Autowired
    private SecretFlowService secretFlowService;

    @Autowired
    private FlFileController flFileController;

    @Autowired
    private FileService fileService;

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
    public String activateMainRay(@RequestParam String port) {
        // 构造命令字符串
        String command = String.format("source sfenv/bin/activate && ray start --head --node-ip-address=\"%s\" --port=\"%s\" --resources='{\"alice\": 16}' --include-dashboard=False --disable-usage-stats", my.getIp(), port);

        // 调用 service 中的方法执行命令
        return secretFlowService.executeCommand(command);
    }

    //需要优化一下 主节点stop连带着其他人也stop
    @GetMapping("/stop-mainRay")
    public String stopMainRay() {
        // 调用 service 中的方法执行命令
        //编写

        return secretFlowService.executeCommand("source sfenv/bin/activate && ray stop");
    }

    @GetMapping("/storeResult")
    public String storeResult(@RequestPart("file") MultipartFile file,@RequestParam("applicationId") String applicationId){
        //调用fileServie中的public String getSha256(MultipartFile file) {
        //
        //        try {
        //            MessageDigest md = MessageDigest.getInstance("SHA-256");
        //            md.update(file.getBytes());
        //            byte[] digest = md.digest();
        //            String mySha256 = DatatypeConverter
        //                    .printHexBinary(digest).toLowerCase();
        //
        //            return mySha256;
        //        } catch (NoSuchAlgorithmException | IOException e) {
        //            e.printStackTrace();
        //            return null;
        //        }
        //    }
        // 生成sha256 并调用flFileController中的@PostMapping("/saveFl")
        //    public Body<String> saveFl(@RequestPart("file") MultipartFile file,
        //                                       @RequestParam("hash") String hash,
        //                                       @RequestParam("applicationId") String applicationId,
        //                                       @RequestParam(value = "expiredTime", required = false) java.sql.Timestamp expiredTime) {
        //
        //        if (expiredTime == null) {
        //            // 设置默认值为当前时间的一周后
        //            expiredTime = java.sql.Timestamp.from(Instant.now().plus(7, ChronoUnit.DAYS));
        //        }
        //
        //        return flFileService.saveFl(file, hash, applicationId, uploadBaseDir, expiredTime);
        //    } 将结果存入到结果缓冲区
        String hash = fileService.getSha256(file);
        if (hash == null) {
            return "Error: Unable to generate SHA-256 hash for the file.";
        }

        // 调用 flFileController 保存文件和生成的 hash
        java.sql.Timestamp expiredTime = null; // 可选的 expiredTime 参数，你可以根据需要传递
        Body<String> result = flFileController.saveFl(file, hash, applicationId,expiredTime);

        // 返回保存结果
        //编写一个排序算




        return "File successfully stored with hash: " + hash;

    }

}
