import com.example.test2.HomeFragment
import com.example.test2.DatabaseHelper
import com.example.test2.SwipeUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HomeFragmentTest {

    private lateinit var fragment: HomeFragment
    private lateinit var mockDbHelper: DatabaseHelper

    @Before
    fun setUp() {
        fragment = HomeFragment()
        mockDbHelper = mockk(relaxed = true)
        fragment.dbHelper = mockDbHelper
    }

    @Test
    fun testLoadUsers() {
        val users = fragment.loadUsers()
        assertEquals(4, users.size)
        assertEquals("user1", users[0].username)
        assertEquals("John Doe", users[0].name)
        assertEquals("john@example.com", users[0].email)
    }

    @Test
    fun testHandleMatch() {
        val username = "testUser"

        every { mockDbHelper.getLoggedInUsername() } returns "loggedInUser"
        every { mockDbHelper.isMatch(any(), any()) } returns false

        fragment.handleMatch(username)

        verify { mockDbHelper.insertMatch("loggedInUser", username) }
    }

    @Test
    fun testHandleMatchAlreadyMatched() {
        val username = "testUser"

        every { mockDbHelper.getLoggedInUsername() } returns "loggedInUser"
        every { mockDbHelper.isMatch(any(), any()) } returns true

        fragment.handleMatch(username)

        verify(exactly = 0) { mockDbHelper.insertMatch(any(), any()) }
    }
}
