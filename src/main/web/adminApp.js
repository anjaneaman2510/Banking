function setAdminDetails(mobileNumber) {
  console.log("setUserDetails");
  const token=localStorage.getItem("jwtTokenAdmin");
  fetch(
    `http://localhost:8080/admin/getByMobile?mobileNumber=${mobileNumber}`,
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
      if (!response.ok) {
        throw new Error("Server Error: " + response.status);
      }
      return response.json();
    })
    .then((data) => {
      // Directly span tag ko Target karein
      localStorage.setItem("adminData", JSON.stringify(data));
      document.getElementById("adminName").innerText =
        data.adminName.toUpperCase();
      document.getElementById("newadminName").value =
        data.adminName.toUpperCase();
      document.getElementById("adminMobile").innerText = data.mobileNumber;
      document.getElementById("newadminMobile").value = data.mobileNumber;
    })

    .catch(() => {});
}

function checkUser(accountNumber) {
  console.log("checkUser");
 const token=localStorage.getItem("jwtTokenAdmin");
  fetch(
    `http://localhost:8080/admin/getByAccount?accountNumber=${accountNumber}`,
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
      if (data === null) {
        document.getElementById("userDetails").style.display = "none";
        showNotification("No user found with this account number.");
      } else {
        console.log(data);
        document.getElementById("userDetails").style.display = "block";
        document.getElementById("userName").innerText = data.userName;

        document.getElementById("userAccount").innerText = accountNumber;

        document.getElementById("userMobile").innerText = data.mobileNumber;

        document.getElementById("userIfsc").innerText = data.ifscCode;

        document.getElementById("userBalance").innerText =
          "₹" + data.balance.toLocaleString();

        document.getElementById("cardStatus").innerText = "Not Issued";

        document.getElementById("userDetails").style.display = "block";
        showNotification("User found successfully.");
      }
    })

    .catch(() => {});
}
function showNotification(message) {
  const notification = document.getElementById("notification");

  notification.innerText = message;

  notification.style.display = "block";

  setTimeout(function () {
    notification.style.display = "none";
  }, 3000);
}
function depositBalanceToUser(accountNumber, amount) {
 const token=localStorage.getItem("jwtTokenAdmin");
  fetch(
    `http://localhost:8080/admin/deposit?accountNumber=${accountNumber}&ammount=${amount}`,
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
          "₹" + amount.toLocaleString("en-IN") + " deposited successfully.",
        );
        document.getElementById("depositAccount").value = "";

        document.getElementById("depositAmount").value = "";
      }
    })

    .catch(() => {});
}
function withdrawBalanceFromUser(accountNumber, amount, pin) {
   const token=localStorage.getItem("jwtTokenAdmin");
  fetch(
    `http://localhost:8080/admin/withdraw?accountNumber=${accountNumber}&ammount=${amount}&pin=${pin}`,
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
