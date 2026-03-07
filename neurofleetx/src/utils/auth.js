// Authentication utilities with localStorage
// Key for storing auth data
const AUTH_KEY = 'neurofleetx_auth';
const USERS_KEY = 'neurofleetx_users';

// Initialize users list if doesn't exist
export const initializeUsers = () => {
  if (!localStorage.getItem(USERS_KEY)) {
    localStorage.setItem(USERS_KEY, JSON.stringify([]));
  }
};

// Register new user
export const registerUser = (email, password, username) => {
  initializeUsers();
  const users = JSON.parse(localStorage.getItem(USERS_KEY) || '[]');
  
  // Check if user already exists
  if (users.find(user => user.email === email)) {
    return { success: false, error: 'User already exists' };
  }
  
  // Create new user
  const newUser = {
    id: Date.now().toString(),
    email,
    password, // Note: In production, this should be hashed
    username,
    createdAt: new Date().toISOString(),
    profile: {
      firstName: '',
      lastName: '',
      bio: '',
      location: '',
      phone: ''
    }
  };
  
  users.push(newUser);
  localStorage.setItem(USERS_KEY, JSON.stringify(users));
  
  return { success: true, user: newUser };
};

// Login user
export const loginUser = (email, password) => {
  initializeUsers();
  const users = JSON.parse(localStorage.getItem(USERS_KEY) || '[]');
  
  const user = users.find(u => u.email === email && u.password === password);
  
  if (!user) {
    return { success: false, error: 'Invalid email or password' };
  }
  
  // Store auth session
  const authData = {
    userId: user.id,
    email: user.email,
    username: user.username,
    loginTime: new Date().toISOString()
  };
  
  localStorage.setItem(AUTH_KEY, JSON.stringify(authData));
  
  return { success: true, user: authData };
};

// Get current authenticated user
export const getCurrentUser = () => {
  const authData = localStorage.getItem(AUTH_KEY);
  if (!authData) {
    return null;
  }
  return JSON.parse(authData);
};

// Check if user is authenticated
export const isAuthenticated = () => {
  return !!localStorage.getItem(AUTH_KEY);
};

// Logout user
export const logoutUser = () => {
  localStorage.removeItem(AUTH_KEY);
  return { success: true };
};

// Get user profile
export const getUserProfile = (userId) => {
  initializeUsers();
  const users = JSON.parse(localStorage.getItem(USERS_KEY) || '[]');
  const user = users.find(u => u.id === userId);
  
  if (!user) {
    return null;
  }
  
  return {
    id: user.id,
    email: user.email,
    username: user.username,
    createdAt: user.createdAt,
    profile: user.profile
  };
};

// Update user profile
export const updateUserProfile = (userId, profileUpdates) => {
  initializeUsers();
  let users = JSON.parse(localStorage.getItem(USERS_KEY) || '[]');
  
  const userIndex = users.findIndex(u => u.id === userId);
  if (userIndex === -1) {
    return { success: false, error: 'User not found' };
  }
  
  users[userIndex].profile = {
    ...users[userIndex].profile,
    ...profileUpdates
  };
  
  localStorage.setItem(USERS_KEY, JSON.stringify(users));
  
  return { success: true, profile: users[userIndex].profile };
};

// Update specific profile field
export const updateProfileField = (userId, fieldName, fieldValue) => {
  initializeUsers();
  let users = JSON.parse(localStorage.getItem(USERS_KEY) || '[]');
  
  const userIndex = users.findIndex(u => u.id === userId);
  if (userIndex === -1) {
    return { success: false, error: 'User not found' };
  }
  
  users[userIndex].profile[fieldName] = fieldValue;
  
  localStorage.setItem(USERS_KEY, JSON.stringify(users));
  
  return { success: true, value: fieldValue };
};

// Validate email format
export const validateEmail = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

// Validate password strength
export const validatePassword = (password) => {
  return {
    isStrong: password.length >= 8,
    hasUpperCase: /[A-Z]/.test(password),
    hasLowerCase: /[a-z]/.test(password),
    hasNumbers: /\d/.test(password),
    hasSpecialChar: /[!@#$%^&*]/.test(password)
  };
};

// Validate username
export const validateUsername = (username) => {
  return username.length >= 3 && username.length <= 20 && /^[a-zA-Z0-9_-]*$/.test(username);
};

// Create sample users for demo (optional)
export const createDemoUsers = () => {
  initializeUsers();
  const users = JSON.parse(localStorage.getItem(USERS_KEY) || '[]');
  
  if (users.length === 0) {
    const demoUsers = [
      {
        id: '1',
        email: 'demo@neurofleetx.com',
        password: 'DemoPassword123!',
        username: 'demo_user',
        createdAt: new Date().toISOString(),
        profile: {
          firstName: 'Demo',
          lastName: 'User',
          bio: 'Welcome to NeuroFleetX! 🚀',
          location: 'San Francisco, CA',
          phone: '+1 (555) 123-4567'
        }
      }
    ];
    
    localStorage.setItem(USERS_KEY, JSON.stringify(demoUsers));
  }
};
