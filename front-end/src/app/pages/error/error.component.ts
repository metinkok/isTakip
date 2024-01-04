import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css']
})
export class ErrorComponent implements OnInit {
  message : String | undefined
  code : String | undefined
  constructor() { }

  ngOnInit(): void {
    this.code = location.href;
    if(this.code.indexOf("error") > 0){//alternatif olarak error kodundan değil hatalı urlden gidilmiştir(404)
      this.code = this.code.substring(this.code.lastIndexOf('/')+1);
      if(this.code == '401'){//JWTnin ip adresi farklı Unauthorized veya Forbidden kullanılıyor Forbidden yetki kontrolünde olduğu için burada Unauthorized
        let home = document.getElementById("Home");
        home?.remove();
        this.code = "401" 
        this.message = "Oturumunuzun farklı bir ip adresinden geldiği tespit edilmiştir, lütfen yeniden oturum açınız."
      }
      else if(this.code == '403'){//JWTnin süresi dolarsa spring sürekli 403 atıyor
        this.message = "Bu sayfaya/işleme erişim yetkiniz bulunmamaktadır. Eğer bu hatayı diğer sayfalarda da almaya devam ederseniz çıkış yap butonuna basıp tekrar giriş yapmanız gerekebilir."
        localStorage.removeItem("admin");
      }
      else if(this.code == '415')
        this.message = "Oluşturmaya çalıştığınız nesne tipi desteklenmemektedir."
      else if(this.code == '500')
        this.message = "Bir sorunla karşılaştık."
      else if(this.code == '400')
        this.message = "Hatalı bir işlem gerçekleştirdiniz."
      else if(this.code == '408')
        this.message = "Yapmaya çalıştığınız işlem zaman aşımına uğradı."
      else if(this.code == '429')
        this.message = "Kısa bir zamanda çok fazla istek gönderdiniz."
      else if(this.code == '507')
        this.message = "Bu veriyi kaydetmek için yeterli hafızamız bulunmamaktadır."
      else if(this.code == '504')//Spring çevrim dışı ya da yapılan isteğe cevap gelmedi
        this.message = "Serverlarımızla bağlantıyı kaybettik."
      else if(this.code == '8492')//Session veya local storage içerisinde yapılan istekle ilgili bilgi yok(Url üzerinden direkt gidilen adreste yüklenecek kullanıcı vs bilgileri doğru gelmemiş)
        this.message = "Yapılan istekte bilgi eksik veya hatalı."
      else if(this.code == '0%20U'){//Spring çevrim dışı ya da yapılan isteğe cevap gelmedi
        this.code = "504"
        this.message = "Serverlarımızla bağlantıyı kaybettik."
      }
      else{//error springden geldi ama yukarıda listeli olanlardan biri değil
        this.message = "Bir sorunla karşılaştık. "+this.code
        this.code = "000"//Olmayan hata kodu; yukarıda yakalanmayan bir kod gelmiş
      }
    }
    else{//404
      this.code = "404"
      this.message = "Erişmeye çaıştığınız sayfa bulunmamaktadır."
    }
  }

  ngHome(){
    location.replace("/home");
  }
  
  ngLogin(){
    localStorage.clear();
    sessionStorage.clear();
    location.replace("/")
  }

}