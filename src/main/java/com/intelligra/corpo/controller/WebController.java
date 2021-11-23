package com.intelligra.corpo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intelligra.corpo.beans.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Controller
@Scope("session")
public class WebController {
@Autowired
RestTemplate restTemplate;
@Value("${corporateuerl}")
String corporateurl;
@Autowired
UserDTO userDTO;

    private final String UPLOAD_DIR = "/uploads/";
    @RequestMapping("index")
    public String index(Model model, HttpServletRequest request )
    {
        request.getSession().invalidate();
      LoginRequest user1 =  new LoginRequest();


  model.addAttribute("user",new LoginRequest());
  // model.addAttribute("content","login::loginfragment");
        return "index";
    }
@PostMapping("login")
    public String login(LoginRequest user, Model model, HttpServletRequest request,RedirectAttributes attributes)
{
    HttpHeaders headers1 = new HttpHeaders();
   /* for (String index : headers.keySet()) {
        headers1.set(index, headers.get(index));
    }*/
     headers1.set("appkey", "corporate");
     headers1.set("content-type","application/json");




    try {
        String data = new ObjectMapper().writeValueAsString(user);
      log.info("{}",data);
        HttpEntity<String> entity = new HttpEntity<>(data, headers1);
        String url = corporateurl+"corporatelogin";

   ResponseEntity<String> ent =restTemplate.exchange(url, HttpMethod.POST,entity,String.class);
if(ent.getStatusCodeValue()==200)
{
    UserDTO userDTO = new ObjectMapper().readValue(ent.getBody(),UserDTO.class);

    userDTO.getUser().setPwd("**********************************");
    model.addAttribute("connectedUser",userDTO);
    log.info("{}",userDTO);

    request.getSession().setAttribute("connectedUser",userDTO);
    return "login";
}
else
{
    attributes.addFlashAttribute("message", "Login Failed and you should check your username and or passwrod. " );
    LoginRequest user1 = new LoginRequest();
    model.addAttribute("user",user1);
    model.addAttribute("message",null);
  log.info("{}",ent);
  return "index"  ;
}


    }
    catch(Exception e)
    {
     //   attributes.addFlashAttribute("message", "Login Failed and you should check your username and or passwrod. " );
        log.info("{}",e);
        model.addAttribute("user",new LoginRequest());
        return "index";
    }
}

@GetMapping("users")
 public String getcorporateusers(Model model,HttpServletRequest request) throws Exception
{
  UserDTO user= (UserDTO)request.getSession().getAttribute("connectedUser");
  log.info("{}",user);
    HttpHeaders headers1 = new HttpHeaders();

    headers1.set("appkey", "corporate");
    headers1.set("authorization","Bearer "+user.getToken());
    headers1.set("content-type","application/json");


    HttpEntity<String> entity = new HttpEntity<>(headers1);
    String url = corporateurl+"corporateusers?coporateid="+user.getUser().getCorporate().getCorporateId();

    ResponseEntity<String> ent =restTemplate.exchange(url, HttpMethod.GET,entity,String.class);
    //log.info("{}",ent);
   if(ent.getStatusCodeValue()==200)
   {
       List<CorporateUser> coporateUsers = new ObjectMapper().readValue(ent.getBody(),new TypeReference<List<CorporateUser>>() {});
       model.addAttribute("users",coporateUsers);
       model.addAttribute("newuser",new CorporateUser());
   }

    return "users";
}

@PostMapping("saveuser")
 public String savetheUser(CorporateUser newuser,HttpServletRequest request)
{
    try {
        UserDTO connectedUser = (UserDTO) request.getSession().getAttribute("connectedUser");
        newuser.setCorporate(connectedUser.getUser().getCorporate());
        newuser.setCorporateId(connectedUser.getUser().getCorporateId());
        newuser.setUserstatus("active");
        newuser.setUsername(newuser.getMsisdn());

        HttpHeaders headers1 = new HttpHeaders();

        headers1.set("appkey", "corporate");
        headers1.set("authorization", "Bearer " + connectedUser.getToken());
        headers1.set("content-type", "application/json");
     //   log.info("{}", newuser);

        String data = new ObjectMapper().writeValueAsString(newuser);
      //  log.info("{}",data);

           log.info("{}",headers1);


        HttpEntity<String> entity = new HttpEntity<>(new ObjectMapper().writeValueAsString(newuser), headers1);
        String urltogenerateotp = corporateurl + "corporateuserotp";
        urltogenerateotp= urltogenerateotp.replace("/corporate/","/").trim();
       //  log.info("{}",urltogenerateotp);
        String urladdcorporate = corporateurl+"corporateuser";
       /// log.info("{}",urladdcorporate);
         ResponseEntity<String> ent1 = restTemplate.exchange(urladdcorporate,HttpMethod.POST,entity,String.class);
       if(ent1.getStatusCodeValue()==200)
       {

          CorporateUser newaddedUser = new ObjectMapper().readValue(ent1.getBody(),CorporateUser.class) ;
          //getting the OTP as password of the user

           HttpEntity<String> entity1 = new HttpEntity<>(data, headers1);
           ResponseEntity<String> ent = restTemplate.exchange(urltogenerateotp, HttpMethod.POST, entity1, String.class);
           if(ent.getStatusCodeValue()==200)
           {
               OTPModel otpModel = new ObjectMapper().readValue(ent.getBody(),OTPModel.class);
               newaddedUser.setPwd(""+otpModel.getOtp());
               HttpEntity<String> entity0 = new HttpEntity<>(new ObjectMapper().writeValueAsString(newaddedUser), headers1);
            ent = restTemplate.exchange(urladdcorporate,HttpMethod.POST,entity0,String.class);
               log.info("{}",newaddedUser);
           }
       }
       else
       {

       }





        return "redirect:/users";
    }
    catch(Exception e)
    {
        System.out.println(e.getMessage());
        return "redirect:/index";
    }
}

@GetMapping("corporatemembers")
    public String getCorporateMembers(Model model, HttpServletRequest request)
{
    List<CorporateMember> members = new ArrayList<>();
    try {
        UserDTO connectedUser = (UserDTO) request.getSession().getAttribute("connectedUser");
        HttpHeaders headers1 = new HttpHeaders();
        String corporatemembersUrl = corporateurl + "corporatemembers?corporateid="+connectedUser.getUser().getCorporate().getCorporateId();
        headers1.set("appkey", "corporate");
        headers1.set("authorization", "Bearer " + connectedUser.getToken());
        headers1.set("content-type", "application/json");
      log.info("{}",corporatemembersUrl);

        HttpEntity<String> entity = new HttpEntity<>(headers1);
        ResponseEntity<String> ent = restTemplate.exchange(corporatemembersUrl, HttpMethod.GET, entity, String.class);
       log.info("{}",ent);
        if (ent.getStatusCodeValue() == 200) {
            String data = ent.getBody();
            members = new ObjectMapper().readValue(data, new TypeReference<List<CorporateMember>>() {
            });
  request.getSession().setAttribute("members",members);

        } else {
           members = new ArrayList<>();
        }
    }
    catch(Exception e)
    {
   members = new ArrayList<>();
   request.getSession().setAttribute("members",members);
    }
CorporateMember activeMember = (CorporateMember) request.getSession().getAttribute("activeMember");
    log.info("IN THE corporatemembers {} ",activeMember);
    if(activeMember==null)
        activeMember = new CorporateMember();
    model.addAttribute("activeMember",activeMember);
     model.addAttribute("newmember",new CorporateMember());
    model.addAttribute("deviceType",new DeviceType());
    request.getSession().setAttribute("deviceType",new DeviceType());
    model.addAttribute("members",members);
    model.addAttribute("offers",new ExtensionCutomerOffer());

    model.addAttribute("categories",new ArrayList<>());
    return "members";
}
@PostMapping("savemember")
public String savemember(CorporateMember member,HttpServletRequest request,RedirectAttributes attributes)
{

        UserDTO connectedUser = (UserDTO) request.getSession().getAttribute("connectedUser");
        member.setCorporate(connectedUser.getUser().getCorporate());
        member.setCorporateId(connectedUser.getUser().getCorporate().getCorporateId());
        member.setMemberId(0);
        member.setStatus("active");

        HttpHeaders headers1 = new HttpHeaders();
        String corporatemembersUrl = corporateurl + "corporatemember";
        log.info("{}", corporatemembersUrl);
        headers1.set("appkey", "corporate");
        headers1.set("authorization", "Bearer " + connectedUser.getToken());
        headers1.set("content-type", "application/json");
    try {
        String data = new ObjectMapper().writeValueAsString(member);
        HttpEntity<String> entity = new HttpEntity<>(data, headers1);
        log.info("{}",data);
        ResponseEntity<String> ent = restTemplate.exchange(corporatemembersUrl, HttpMethod.POST, entity, String.class);
        log.info("{}",data);
    }

    catch(Exception e)
    {
        attributes.addFlashAttribute("message", "Please select a file to upload.");
    }


    return "redirect:/corporatemembers";
}

    @PostMapping("/templateupload")
    public String uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request,RedirectAttributes attributes) {

        UserDTO connectedUser = (UserDTO) request.getSession().getAttribute("connectedUser");
        // check if file is empty
        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/corporatemembers";
        }

        // normalize the file path
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String dir = System.getProperty("user.dir");
        // save the file on the local file system
        try {
      String spath =dir+"/"+ fileName;
      log.info("{}",dir);
           spath= spath.replace("file:/","");
            File newfile = new File(spath);
            Path path = Paths.get(spath);
            log.info("{}",spath);
            if(!newfile.exists())
            newfile.createNewFile();

//Files.copy(file.getInputStream(),newfile.toPath());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            List<CorporateMember> newmembers = new ArrayList<>();
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(0);

            for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++) {
                CorporateMember member = new CorporateMember();

                XSSFRow row = worksheet.getRow(i);
                member.setMemberId(0);
                member.setName((String) row.getCell(1).getStringCellValue());
                member.setSurname((String)row.getCell(2).getStringCellValue());
                member.setMemberEmail((String)row.getCell(3).getStringCellValue());
                member.setAddress((String)row.getCell(4).getStringCellValue());
                member.setNational_id_number((String)row.getCell(5).getStringCellValue());
                member.setMsisdn((String)row.getCell(5).getStringCellValue());
                member.setIdentification_type("1");
                member.setStatus("active");
                member.setCorporate(connectedUser.getUser().getCorporate());
                member.setCorporateId(connectedUser.getUser().getCorporateId());
  if(member.getSurname().equalsIgnoreCase("surname"))
      continue;
if(member.getName().isEmpty() || member.getName().trim().length()==0)
    continue;
                newmembers.add(member);
                log.info("{}",member);
            }
            HttpHeaders headers1 = new HttpHeaders();
            String addbulkymembersUrl = corporateurl + "addcorporatebulkymembers";


            headers1.set("appkey", "corporate");
            headers1.set("authorization", "Bearer " + connectedUser.getToken());
            headers1.set("content-type", "application/json");

                String data = new ObjectMapper().writeValueAsString(newmembers);
                HttpEntity<String> entity = new HttpEntity<>(data, headers1);
                log.info("{}",data);
                ResponseEntity<String> ent = restTemplate.exchange(addbulkymembersUrl, HttpMethod.POST, entity, String.class);
                log.info("{}",ent.getStatusCodeValue());


   attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!');

        } catch (IOException e) {
            System.out.println("HERE THERE HAS BEEN AN ERROR : "+e.getMessage());
            e.printStackTrace();
        }


        return "redirect:/corporatemembers";
    }

@GetMapping("showdevicecategories")
    public String getDevicecategories(@RequestParam("memberid")int memberid, Model model, HttpServletRequest request) throws Exception
{
    System.out.println("THIS HAS BEEN CALLED");
 UserDTO connectedUser = (UserDTO) request.getSession().getAttribute("connectedUser");
    HttpHeaders headers1 = new HttpHeaders();
    String corporatemembersUrl = corporateurl + "corporatememberbyid?memberid="+memberid;
    headers1.set("appkey", "corporate");
    headers1.set("authorization", "Bearer " + connectedUser.getToken());
    headers1.set("content-type", "application/json");
   // log.info("{}",corporatemembersUrl);

    HttpEntity<String> entity = new HttpEntity<>(headers1);
  //  log.info("{}",corporatemembersUrl);

    ResponseEntity<String> ent = restTemplate.exchange(corporatemembersUrl, HttpMethod.GET, entity, String.class);

//log.info("{}",ent);
if(ent.getStatusCodeValue()==200)
{
    CorporateMember activeMember = new CorporateMember();
    activeMember = new ObjectMapper().readValue(ent.getBody(),CorporateMember.class);
    //model.addAttribute("activeMember",activeMember);
      request.getSession().setAttribute("activeMember",activeMember);
   // log.info("THE CORPORATE MEMBER WAS FOUND WITH A RESPONSE CODE "+ent.getStatusCodeValue());
   // log.info("{}",activeMember);
    String devicecatetoriesUlr = corporateurl + "alldevicetypecategories";
    devicecatetoriesUlr =devicecatetoriesUlr.replace("/corporate/","/");
    HttpEntity<String> entity1 = new HttpEntity<>(headers1);
     log.info("{}",devicecatetoriesUlr);

    ResponseEntity<String> ent1 = restTemplate.exchange(devicecatetoriesUlr, HttpMethod.GET, entity1, String.class);
    List<DeviceTypeCategory> categories = new ObjectMapper().readValue(ent1.getBody(),new TypeReference<List<DeviceTypeCategory>>(){});
    request.getSession().setAttribute("categories",categories);

      ///////////////////////////////////////////// DEVICES OF THE FIRST CHOSEN CATEGORY//////////////////////////////////////

    String devicetypeurl = corporateurl + "corporatemsisdnplans?msisdn=0788636644";
    devicetypeurl =devicetypeurl.replace("/corporate/","/");
    HttpEntity<String> entity2 = new HttpEntity<>(headers1);
    log.info("{}",devicetypeurl);

    ResponseEntity<String> ent2 = restTemplate.exchange(devicetypeurl, HttpMethod.GET, entity2, String.class);
   // log.info("{}",ent2);
    if(ent2.getStatusCodeValue()==200)
    {
        ExtensionCutomerOffer customeroffers = new ObjectMapper().readValue(ent2.getBody(),ExtensionCutomerOffer.class);
        request.getSession().setAttribute("offers",customeroffers);
    log.info("{}",customeroffers);
     }

   // List<DeviceTypeCategory> categories = new ObjectMapper().readValue(ent1.getBody(),new TypeReference<List<DeviceTypeCategory>>(){});
   // request.getSession().setAttribute("categories",categories);






   log.info("THE CATEGORIES OF THE DEVICES: {}",ent1.getStatusCodeValue());
}




    return "redirect:/modals";
}
@GetMapping("modals")
public String savemodals(Model model,HttpServletRequest request)
{
    UserDTO connectedUser = (UserDTO) request.getSession().getAttribute("connectedUser");
    CorporateMember activeMember = (CorporateMember) request.getSession().getAttribute("activeMember");

    List<CorporateMember> members =(List<CorporateMember>) request.getSession().getAttribute("members");
    model.addAttribute("activeMember",activeMember);
    model.addAttribute("newmember",new CorporateMember());
    model.addAttribute("members",members);
    model.addAttribute("categories",(List<DeviceTypeCategory>)request.getSession().getAttribute("categories"));
    model.addAttribute("deviceType",(DeviceType)request.getSession().getAttribute("deviceType"));
model.addAttribute("offers",(ExtensionCutomerOffer)request.getSession().getAttribute("offers"));
    log.info("active member: {} and the number of members {}",activeMember.getName(),members.size());


    return "modals";
}



}
