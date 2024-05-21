/**
 * @module _Functions/User
 */
import { encryptPass } from "../plugins/hashpass.js";

/**
 * Register new user
 *
 * @memberof module:_Functions/User
 * @function registerUser
 * @param {string} username 
 * @param {string} email 
 * @param {string} password 
 * @return {boolean} 
 */
export async function registerNewUser(newUser) {
  const apiUrl = import.meta.env.VITE_API_URL;
  newUser.pass = encryptPass(newUser.pass);
  const options = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(newUser),
  };

  const response = await fetch(`${apiUrl}/user`, options);

  const data = await response.json();

  if (data.hasOwnProperty("username")) {
    return true;
  } else {
    return false;
  }
}

/**
 * login session
 *
 * @memberof module:_Functions/User
 * @function loginUser
 * @param {string} email 
 * @param {string} password 
 * @return {object} 
 **/
export async function loginUser(email, password) {
  const apiUrl = import.meta.env.VITE_API_URL;
  const options = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      email: email,
      pass: encryptPass(password),
    }),
  };
  const response = await fetch(`${apiUrl}/user/login`, options);
  const data = await response.json();

  if (data.hasOwnProperty("code")) {
    return false;
  } else {
    const expires = new Date();
    expires.setDate(expires.getDate() + 7);
    const cookie = `loggedUser=${encodeURIComponent(
      data.id
    )}; expires=${expires.toUTCString()}; path=/; SameSite=None; Secure`;
    document.cookie = cookie;
    sessionStorage.setItem("loggedUser", JSON.stringify(data));
    return true;
  }
}

/**
 * Close session
 *
 * @memberof module:_Functions/User
 * @function logoutUser
 */
export function logoutUser() {
  const expires = new Date();
  expires.setDate(expires.getDate() - 1);
  const cookie = `loggedUser=; expires=${expires.toUTCString()}; path=/; SameSite=None; Secure`;
  document.cookie = cookie;
  sessionStorage.removeItem("loggedUser");
}

/**
 * get user data
 *
 * @memberof module:_Functions/User
 * @function fetchUser
 * @param {string} idUser 
 * @return {object} 
 */
export async function fetchUser(idUser) {
  const apiUrl = import.meta.env.VITE_API_URL;

  const response = await fetch(`${apiUrl}/user?idUser=${idUser}`);
  const data = await response.json();

  if (data.hasOwnProperty("username")) {
    sessionStorage.setItem("loggedUser", JSON.stringify(data));
  } else {
    return;
  }
}

/**
 * return data from user store
 *
 * @memberof module:_Functions/User
 * @function getLoggedUser
 * @return {object} 
 */
export function getLoggedUser() {
  const loggedUser = sessionStorage.getItem("loggedUser");
  if (loggedUser) {
    return JSON.parse(loggedUser);
  } else {
    return null;
  }
}
