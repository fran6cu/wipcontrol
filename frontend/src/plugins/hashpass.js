import CryptoJS from "crypto-js";
/**
 * @module _Functions/PassOperations
 */

/**
 * Encrypt password using sha256
 *
 * @memberof module:_Functions/PassOperations
 * @function
 * @param {string} password Password encrypted
 * @return {string} 
 */
const encryptPass = (password) => {
  const encryptedPass = CryptoJS.SHA256(password).toString();
  return encryptedPass;
};

export { encryptPass };
