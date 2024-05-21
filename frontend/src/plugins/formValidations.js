/**
 * @module _Functions/Validations
 */

/**
 * check email
 *
 * @memberof module:_Functions/Validations
 * @function
 * @param {string} email Email to validate
 * @return {boolean}
 */
export const validateEmailRegex = (email) => {
  // Regular expression check valid email
  const emailRegex = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/g;
  return emailRegex.test(email);
};

/**
 * check pass using regular expression
 *
 * @memberof module:_Functions/Validations
 * @function
 * @param {string} password 
 * @return {boolean}
 */
export const validatePasswordRegex = (password) => {
  // check pass format, lenght, characters, etc.
  const passRegex =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&\.\-\_])[A-Za-z\d@$!%*?&\.\-\_]{8,32}$/;
  return passRegex.test(password);
};

/**
 * Check double equal password 
 *
 * @memberof module:_Functions/Validations
 * @function
 * @param {string} check 
 * @param {string} pass 
 * @return {boolean}
 */
export const validateSamePassRegex = (check, pass) => {
  return check === pass;
};

/**
 * Check user name 
 *
 * @memberof module:_Functions/Validations
 * @function
 * @param {string} user 
 * @return {boolean}
 */
export const validateUserRegex = (user) => {
  // user name format and characters
  const userRegex = /^[A-Za-z\d]{5,30}$/;
  return userRegex.test(user);
};
