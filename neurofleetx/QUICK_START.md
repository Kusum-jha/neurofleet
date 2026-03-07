# NeuroFleetX - Quick Start Guide

## 🎯 Quick Start

### 1. Install Dependencies
```bash
npm install
```

### 2. Start Development Server
```bash
npm run dev
```
Server starts at: `http://localhost:5173`

### 3. Login with Demo Credentials
- **Email**: `demo@neurofleetx.com`
- **Password**: `DemoPassword123!`

### 4. Explore Features
- ✅ View your profile
- ✅ Edit profile information (click any field)
- ✅ Navigate between pages
- ✅ Logout and register new account

---

## 🚀 Available Scripts

| Command | Purpose |
|---------|---------|
| `npm run dev` | Start development server with HMR |
| `npm run build` | Build for production |
| `npm run preview` | Preview production build |
| `npm run lint` | Run ESLint to check code quality |

---

## 📋 User Flows

### **New User Registration**
1. Click "Register" in navbar
2. Fill in all fields:
   - Email
   - Username (3-20 chars, alphanumeric + - _)
   - Password (8+ chars, upper, lower, number)
   - Confirm password
3. Click "Create Account"
4. Automatically redirected to login

### **Login**
1. Visit login page
2. Enter registered email & password
3. Click "Sign In"
4. Redirected to home page

### **Profile Editing**
1. After login, click "Profile" in navbar
2. Click any profile field to edit:
   - First Name, Last Name, Bio, Location, Phone
3. Type new value
4. Click "✓ Save" to confirm
5. Success message appears

### **Logout**
Option 1: Click "Logout" button in navbar
Option 2: Click "🚪 Logout" on profile page

---

## 🎨 Customization Cheat Sheet

### Change Theme Colors
**File**: `src/styles/theme.css`

```css
:root {
  --ocean-dark: #001d3d;      /* Change these */
  --ocean-light: #0066cc;
  --ocean-lighter: #4da6ff;
  /* ... etc */
}
```

### Add New Page
1. Create `src/pages/YourPage.jsx`
2. Add route in `src/App.jsx`:
```jsx
<Route path="/yourpage" element={<YourPage />} />
```
3. Update navigation in `src/components/Navigation.jsx`

### Modify Form Fields
Edit profile fields in `src/pages/Profile.jsx`:
```jsx
const profileFields = [
  { key: 'fieldName', label: 'Field Label', icon: '🎯' },
  // Add more fields here
];
```

---

## 🔍 Project Structure Map

```
src/
├── pages/        → Page components (login, register, home, profile)
├── components/   → Reusable components (nav, protected route)
├── styles/       → CSS files (theme, pages, components)
├── utils/        → Auth logic & utilities
├── App.jsx       → Router setup
└── main.jsx      → Entry point
```

---

## ⚡ Key Features Explained

### Glassmorphism Design
- Frosted glass effect with `backdrop-filter: blur()`
- Semi-transparent backgrounds
- Subtle shadows and borders
- Smooth hover animations

### LocalStorage Authentication
- No backend required
- User data stored in browser
- Demo user included
- Complete CRUD operations for profiles

### Bubbly UI Elements
- Large border radius (rounded corners)
- Soft colors from ocean blue palette
- Smooth transitions and animations
- Gen Z-friendly modern design

### Responsive Design
- Mobile-first CSS
- Breakpoint at 768px
- Touch-friendly button sizes
- Adaptive layouts

---

## 🐛 Troubleshooting

### Port Already in Use
```bash
# Use different port
npm run dev -- --host 0.0.0.0 --port 3000
```

### Node Version Issues
Current version requires Node 18.20+
Update with: `nvm install 20.19.0`

### Build Failing
```bash
# Clear cache and reinstall
rm -rf node_modules package-lock.json
npm install
npm run build
```

### Styles Not Loading
Check that all CSS imports are in `src/App.jsx`:
```jsx
import './styles/theme.css';
import './styles/pages.css';
import './styles/components.css';
```

---

## 📊 Performance Tips

1. **Development**: Use `npm run dev` for fast HMR
2. **Production**: Run `npm run build` for optimization
3. **Bundle Size**: Currently ~230KB (gzipped ~72KB)
4. **CSS**: Custom CSS only (~16KB gzipped)

---

## 🔐 Security Notes

⚠️ **This is a demo application with localStorage authentication**

For production:
- Use real backend authentication (JWT, OAuth, etc.)
- Hash passwords with bcrypt or similar
- Use HTTPS only
- Implement CSRF protection
- Add rate limiting

---

## 📞 Support

For issues or questions:
1. Check `PROJECT_DOCUMENTATION.md` for detailed info
2. Review `src/utils/auth.js` for auth system details
3. Check CSS variables in `src/styles/theme.css`

---

## 🎉 Next Steps

1. ✅ Install dependencies
2. ✅ Start dev server
3. ✅ Test with demo account
4. ✅ Customize colors/branding
5. ✅ Add new features
6. ✅ Deploy to production

---

**Happy Coding! 🚀**
