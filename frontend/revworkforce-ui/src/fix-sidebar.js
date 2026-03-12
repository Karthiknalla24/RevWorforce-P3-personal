const fs = require('fs');
const path = require('path');

function processFile(filePath) {
  let content = fs.readFileSync(filePath, 'utf8');
  let changed = false;

  // Add toggle functionality to .ts files
  if (filePath.endsWith('.ts') && content.includes('SidebarComponent')) {
    if (!content.includes('isSidebarOpen = false;')) {
      content = content.replace(/}\s*$/, '\n  isSidebarOpen = false;\n  toggleSidebar() { this.isSidebarOpen = !this.isSidebarOpen; }\n}\n');
      changed = true;
    }
  }

  // Add toggle and overlay to .html files
  if (filePath.endsWith('.html') && content.includes('<app-sidebar')) {
    let original = content;
    
    // Add overlay and bind isOpen
    if (!content.includes('sidebar-overlay')) {
      content = content.replace(/<app-sidebar([^>]*)>/, '<div class="sidebar-overlay" [class.sidebar-open]="isSidebarOpen" (click)="toggleSidebar()"></div>\n  <app-sidebar$1 [isOpen]="isSidebarOpen">');
    }

    // Add toggle button to topbar
    if (!content.includes('menu-toggle') && content.includes('class="topbar-right"')) {
      content = content.replace(/<div class="topbar-right">/, '<div class="topbar-right">\n        <button class="menu-toggle" (click)="toggleSidebar()">&#9776;</button>');
    }

    if (content !== original) {
      content = content.replace(/<div class="topbar-right">\s*<div class="topbar-avatar"/g, '<div class="topbar-right">\n        <button class="menu-toggle" (click)="toggleSidebar()">&#9776;</button>\n        <div class="topbar-avatar"');
      changed = true;
    }
  }

  if (changed) {
    fs.writeFileSync(filePath, content);
    console.log('Updated', filePath);
  }
}

function walkDir(dir) {
  const files = fs.readdirSync(dir);
  for (const file of files) {
    const fullPath = path.join(dir, file);
    if (fs.statSync(fullPath).isDirectory()) {
      walkDir(fullPath);
    } else if (file.endsWith('.ts') || file.endsWith('.html')) {
      if (!fullPath.includes('dashboard.component') && !fullPath.includes('profile.component') && !fullPath.includes('sidebar.component')) {
        processFile(fullPath);
      }
    }
  }
}

walkDir('./app');
