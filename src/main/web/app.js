function setUserDetails(mobileNumber) {
  console.log("setUserDetails");
    const token=localStorage.getItem("jwtToken");
  fetch(`http://localhost:8080/user/getByMobile?number=${mobileNumber}`, {
    method: "GET",
    credentials: "include",
     headers: {
        "Authorization": `Bearer ${token}`
    }
  })
    .then((response) => {
      if (response.status === 401) {
        throw new Error("Unauthorized: Basic Auth Cancelled or Failed");
      }
      if (!response.ok) {
        throw new Error("Server Error: " + response.status);
      }
      return response.json();
    })
    .then((data) => {
      // Directly span tag ko Target karein
      localStorage.setItem("userData", JSON.stringify(data));
      setLoginStatus(data.accountNumber);
      recentTransectionSet();
      document.getElementById("nameSpan").textContent =
        " " + data.userName.split(" ")[0];
      document.getElementById("amountSpan").innerText = Number(
        data.balance,
      ).toLocaleString("en-IN");
      document.getElementById("accountSpan").innerText =
        data.accountNumber.slice(0, 2) + "XXXXX" + data.accountNumber.slice(-4);
      document.getElementById("mobileSpan").innerText =
        data.mobileNumber.slice(-4);
    })

    .catch(() => {});
}

function setUserDetailsByAccount(accountNumber) {
  console.log("setUserDetailsByAccount");
  const token = localStorage.getItem("jwtToken");

console.log("TOKEN:", token);

fetch(`http://localhost:8080/user/getByAccount?accountNumber=${accountNumber}`, {
    method: "GET",  
  headers: {
        "Authorization": `Bearer ${token}`
    }
})
.then(async response => {
    console.log("STATUS:", response.status);

    const text = await response.text();
    console.log("RESPONSE:", text);

    if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${text}`);
    }

    return JSON.parse(text);
})
.then(data => {
    console.log("USER DATA:", data);
    
      // Directly span tag ko Target karein
     
      localStorage.setItem("userData", JSON.stringify(data));
      setLoginStatus(data.accountNumber);
      console.log("userDataSet");
      recentTransectionSet();
      document.getElementById("nameSpan").textContent =
        " " + data.userName.split(" ")[0];
      document.getElementById("amountSpan").innerText = Number(
        data.balance,
      ).toLocaleString("en-IN");
      document.getElementById("accountSpan").innerText =
        data.accountNumber.slice(0, 2) + "XXXXX" + data.accountNumber.slice(-4);
      document.getElementById("mobileSpan").innerText =
        data.mobileNumber.slice(-4);
    } 
)
.catch(error => {
    console.error("API ERROR:", error);
});
}
function setUserDetailsProfile() {
  const userData = JSON.parse(localStorage.getItem("userData"));
  console.log(userData);
  console.log("setUserDetailsProfile");
  document.getElementById("avatar").innerText = userData.userName
    .slice(0, 1)
    .toUpperCase();
  document.getElementById("displayName").innerText = userData.userName;
  document.getElementById("name").value = userData.userName;
  document.getElementById("account").value = userData.accountNumber;
  document.getElementById("mobile").value = userData.mobileNumber;
  document.getElementById("ifsc").value = userData.ifscCode;
}

function setUserAcountDetails() {
  const userData = JSON.parse(localStorage.getItem("userData"));
  console.log(userData);
  console.log("setUserAccountDetails");

  document.getElementById("name").innerText = userData.userName;
  document.getElementById("mobile").innerText = userData.mobileNumber;
  document.getElementById("account").innerText = userData.accountNumber;
  document.getElementById("ifsc").innerText = userData.ifscCode;
  document.getElementById("balance").innerText =
    "₹" + Number(userData.balance).toLocaleString("en-IN");
  const status = document.getElementById("status");
  if (userData.accountStatus) {
    status.innerText = "Active";
    status.style.color = "#35d0ba";
  } else {
    status.innerText = "Block";
    status.style.color = "#ff647c";
  }
}

function depositBalanceToUser(amount, pin) {
    const token=localStorage.getItem("jwtToken");
  const userData = JSON.parse(localStorage.getItem("userData"));
  fetch(
    `http://localhost:8080/user/depositUsingPin?accountNumber=${userData.accountNumber}&ammount=${amount}&pin=${pin}`,
    {
      method: "PATCH",
      credentials: "include",
       headers: {
        "Authorization": `Bearer ${token}`
    }
    },
  )
    .then((response) => {
      if (response.status === 401) {
        throw new Error("Unauthorized: Basic Auth Cancelled or Failed");
      }
      if (response.status === 404) {
        // User nahi mila
        return null;
      }
      if (!response.ok) {
        throw new Error("Server Error: " + response.status);
      }
      return response.json();
    })
    .then((data) => {
      if (data) {
        alert("Deoposit SuccesFully.....");
        window.location.href = "user-dashboard.html";
      } else {
        alert("Wrong PIN....");
      }
    })

    .catch(() => {});
}

function withdrawBalanceFromUser(amount, pin) {
    const token=localStorage.getItem("jwtToken");
  const userData = JSON.parse(localStorage.getItem("userData"));
  fetch(
    `http://localhost:8080/user/withdraw?accountNumber=${userData.accountNumber}&ammount=${amount}&pin=${pin}`,
    {
      method: "PATCH",
      credentials: "include",
       headers: {
        "Authorization": `Bearer ${token}`
    }
    },
  )
    .then((response) => {
      if (response.status === 401) {
        throw new Error("Unauthorized: Basic Auth Cancelled or Failed");
      }
      if (response.status === 404) {
        // User nahi mila
        return null;
      }
      if (!response.ok) {
        throw new Error("Server Error: " + response.status);
      }
      return response.json();
    })
    .then((data) => {
      if (data) {
        showNotification(
          "₹" + amount.toLocaleString("en-IN") + " withdrawl successfully.",
        );
        document.getElementById("withdrawAccount").value = "";

        document.getElementById("withdrawAmount").value = "";
        document.getElementById("withdrawPIN").value = "";
      }
    })

    .catch(() => {});
}

function withdrawBalanceFromUser(accountNumber, amount, pin) {
    const token=localStorage.getItem("jwtToken");
  console.log("withdrawBalanceFromUser app.js");
  fetch(
    `http://localhost:8080/user/withdraw?accountNumber=${accountNumber}&ammount=${amount}&pin=${pin}`,
    {
      method: "PATCH",
      credentials: "include",
       headers: {
        "Authorization": `Bearer ${token}`
    }
    },
  )
    .then((response) => {
      if (response.status === 401) {
        throw new Error("Unauthorized: Basic Auth Cancelled or Failed");
      }
      if (response.status === 404) {
        // User nahi mila
        return null;
      }
      if (!response.ok) {
        throw new Error("Server Error: " + response.status);
      }
      return response.json();
    })
    .then((data) => {
      if (data) {
        alert("Withdrawal  successfully!");
        window.location.href = "user-dashboard.html";
      } else {
        alert("Invalid PIN.....X");
      }
    })

    .catch(() => {});
}
function formatDateTime(dateString) {
  const date = new Date(dateString);

  const formattedDate = date.toLocaleDateString("en-GB", {
    day: "2-digit",
    month: "short",
    year: "numeric",
  });

  const formattedTime = date.toLocaleTimeString("en-US", {
    hour: "2-digit",
    minute: "2-digit",
    hour12: true,
  });

  return `${formattedDate} <span class="time-text">| ${formattedTime}</span>`;
}
function transectionsList() {
  console.log("transectionList");
    const token=localStorage.getItem("jwtToken");
  const userData = JSON.parse(localStorage.getItem("userData"));
  fetch(
    `http://localhost:8080/user/transection?number=${userData.accountNumber}`,
    {
      method: "GET",
      credentials: "include",
       headers: {
        "Authorization": `Bearer ${token}`
    }
    },
  )
    .then((response) => {
      if (response.status === 401) {
        throw new Error("Unauthorized: Basic Auth Cancelled or Failed");
      }
      if (response.status === 404) {
        // User nahi mila
        return null;
      }
      if (!response.ok) {
        throw new Error("Server Error: " + response.status);
      }
      return response.json();
    })
    .then((data) => {
      const tbody = document.getElementById("tableBody");
      tbody.innerHTML = "";

      data.forEach((item) => {
        const isCredit = item.type.toLowerCase() === "credit";
        const amountClass = isCredit ? "credit" : "debit";
        const amountSign = isCredit ? "+" : "-";
        const statusClass =
          item.status.toLowerCase() === "success" ? "success" : "failed";

        const row = `
            <tr>
                <td>${formatDateTime(item.transectionTime)}</td>
                <td>TXN1000${item.id}</td>
                <td>${item.Discription}</td>
                <td>${item.type}</td>
                <td class="${amountClass}">${amountSign}₹${item.amount.toLocaleString()}</td>
                <td class="${statusClass}">${item.status}</td>
            </tr>
        `;
        tbody.innerHTML += row;
      });
    })

    .catch(() => {});
}

function recentTransectionSet() {
  console.log("recentTransectionSet");
  const userData = JSON.parse(localStorage.getItem("userData"));
  const token=localStorage.getItem("jwtToken");
  fetch(
    `http://localhost:8080/user/transection3?number=${userData.accountNumber}`,
    {
      method: "GET",
      credentials: "include",
       headers: {
        "Authorization": `Bearer ${token}`
    }
    },
  )
    .then((response) => {
      if (response.status === 401) {
        throw new Error("Unauthorized: Basic Auth Cancelled or Failed");
      }
      if (response.status === 404) {
        // User nahi mila
        return null;
      }
      if (!response.ok) {
        throw new Error("Server Error: " + response.status);
      }
      return response.json();
    })
    .then((data) => {
      const tbody = document.getElementById("tableBodyFor3");
      tbody.innerHTML = "";

      data.forEach((item) => {
        const isCredit = item.type.toLowerCase() === "credit";
        const amountClass = isCredit ? "credit" : "debit";
        const amountSign = isCredit ? "+" : "-";
        const statusClass =
          item.status.toLowerCase() === "success" ? "success" : "failed";

        const row = `
            <tr>
                <td>${formatDateTime(item.transectionTime)}</td>
                <td>TXN1000${item.id}</td>
                <td class="${amountClass}">${amountSign}₹${item.amount.toLocaleString()}</td>
                <td class="${statusClass}">${item.status}</td>
            </tr>
        `;
        tbody.innerHTML += row;
      });
    })

    .catch(() => {});
}

function transferToAnother(receiverAccount, amount, description, PIN) {
  console.log("transferSection");
  const userData = JSON.parse(localStorage.getItem("userData"));
    const token=localStorage.getItem("jwtToken");
  fetch(
    `http://localhost:8080/user/transfer?accountNumber=${userData.accountNumber}&amount=${amount}&pin=${PIN}&receverAccount=${receiverAccount}&description=${description}`,
    {
      method: "PATCH",
      credentials: "include",
       headers: {
        "Authorization": `Bearer ${token}`
    }
    },
  )
    .then((response) => {
      if (response.status === 401) {
        throw new Error("Unauthorized: Basic Auth Cancelled or Failed");
      }
      if (response.status === 404) {
        // User nahi mila
        return null;
      }
      if (!response.ok) {
        throw new Error("Server Error: " + response.status);
      }
      return response.json();
    })
    .then((data) => {
      if (data == 2) {
        alert("Transfer successfully!");
        window.location.href = "user-dashboard.html";
      } else if (data == 1) {
        alert("Invalid PIN.....X");
        return;
      } else {
        alert("Recevers Account Number Wrong....");
      }
    })

    .catch(() => {});
}

function setLoginStatus(account)
{
    const token=localStorage.getItem("jwtToken");
   console.log("setLoginStatus");  
   fetch(`http://localhost:8080/user/setLoginStatus?accountNumber=${account}`,
            {
              method: "PATCH",
              credentials: "include",
               headers: {
        "Authorization": `Bearer ${token}`
    }
            },
          )
          
            .catch((error) => {
              console.error(error.message);
            });
}